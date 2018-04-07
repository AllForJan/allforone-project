package com.vaadin.starter.beveragebuddy.backend.rpvs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.starter.beveragebuddy.backend.PriamaPlatba;
import com.vaadin.starter.beveragebuddy.backend.Ziadatel;
import com.vaadin.starter.beveragebuddy.backend.ZiadostiService;

import elemental.util.Collections;

public class RpvsService {

	private static class SingletonHolder {
		static final RpvsService INSTANCE = createDemoService();

		private SingletonHolder() {
		}

		private static RpvsService createDemoService() {

			ZiadostiService.getInstance();

			Map<String, List<PoberatelVyhod>> json = new HashMap<>();
			final ObjectMapper mapper = new ObjectMapper();
			try {
				json = mapper.readValue(new File("data/rpvs_2018-04-07.json"),
						new TypeReference<Map<String, List<PoberatelVyhod>>>() {
						});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Map<PoberatelVyhod, List<String>> poberateloveFirmy = new HashMap<>();
			for (String ico : json.keySet()) {
				List<PoberatelVyhod> poberatelia = json.get(ico);
				for (PoberatelVyhod poberatel : poberatelia) {
					List<String> zoznamFiriem = poberateloveFirmy.get(poberatel);
					if (zoznamFiriem == null) {
						zoznamFiriem = new ArrayList<>();
					}
					zoznamFiriem.add(ico);
					poberateloveFirmy.put(poberatel, zoznamFiriem);
				}
			}

			List<PoberatelSumar> poberateliaCelkovo = new ArrayList<>();
			for (PoberatelVyhod poberatel : poberateloveFirmy.keySet()) {
				PoberatelSumar sumar = new PoberatelSumar();
				sumar.setPoberatel(poberatel);
				for (String ico : poberateloveFirmy.get(poberatel)) {
					FirmaPlatby platbyFirme = new FirmaPlatby();
					Ziadatel ziadatel = ZiadostiService.getInstance().findZiadatel(ico);
					if (ziadatel == null) {
						continue;
					}
					platbyFirme.getPriamePlatby()
							.addAll(ZiadostiService.getInstance().findPriamaPlatba(ziadatel.getZiadatel()));
					platbyFirme.setIco(ico);
					platbyFirme.setNazov(ziadatel.getZiadatel());
					sumar.getPlatbyFirmam().add(platbyFirme);
				}
				poberateliaCelkovo.add(sumar);
			}

			poberateliaCelkovo.sort(Comparator.comparing(PoberatelSumar::getSumaVsetkychPlatieb));

			try (Writer out = new OutputStreamWriter(new FileOutputStream("data/poberatelia.txt"), "UTF8")) {
				for (PoberatelSumar sumar : poberateliaCelkovo) {
					out.write(sumar.getPoberatel().toString() + "\t" + sumar.getSumaVsetkychPlatieb() + " EUR\n"
							+ poberateloveFirmy.get(sumar.getPoberatel()) + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (Writer out = new OutputStreamWriter(new FileOutputStream("data/ico_nazvy.txt"), "UTF8")) {
				for (Ziadatel z : ZiadostiService.getInstance().findZiadatelov(null, 0, 0)) {
					out.write(z.getIco() + "\t" + z.getDalsieNazvy() + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			final RpvsService service = new RpvsService();
			// nacitajPriamePlatby(service);
			return service;
		}

		/*
		 * private static void nacitajPriamePlatby(RpvsService service) { int i = 0; try
		 * { //URL;Meno;PSC;Obec;Opatrenie;Opatrenie - Kod;Suma;Rok Scanner scanner =
		 * new Scanner(new File("c:/tmp/dataFIIT/apa_prijimatelia_2018-03-15.csv"),
		 * "UTF-8");
		 * 
		 * String line; scanner.nextLine(); while (scanner.hasNext()) { i++; line =
		 * scanner.nextLine().replace("&quot;", "").replace("&amp;", ""); String[]
		 * fields = line.split(";"); if (fields.length >= 8) { PriamaPlatba pp = new
		 * PriamaPlatba(); pp.setUrl(fields[0]); pp.setZiadatel(fields[1]);
		 * pp.setPsc(fields[2]); pp.setObec(fields[3]); pp.setOpatrenie(fields[4]);
		 * pp.setKodOpatrenia(fields[5]); String strPlatba = fields[6]; try {
		 * pp.setSuma(new BigDecimal(strPlatba)); } catch (NumberFormatException ex) {
		 * pp.setSuma(new BigDecimal("0.0000000000001"));
		 * System.err.println("Invalid parsing bigdecimal: " + line + " riadok >" + i);
		 * } try { pp.setRok(Integer.parseInt(fields[7])); } catch
		 * (NumberFormatException ex) { pp.setRok(-1);
		 * System.err.println("Invalid parsing of int: " + line + " riadok >" + i); }
		 * service.savePriamaPlatba(pp); } else { System.err.println("Invalid record: "
		 * + line + " riadok >" + i + " , " + fields.length); } } scanner.close(); }
		 * catch (Exception e) { System.out.println(e); e.printStackTrace(); } }
		 */
	}

	private Map<Long, PriamaPlatba> listPriamychPlatieb = new HashMap<>();

	private AtomicLong nextId = new AtomicLong(0);

	private RpvsService() {
	}

	public static RpvsService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public List<PriamaPlatba> findPriamaPlatba(String filter) {

		if (filter != null) {
			String normalizedFilter = filter.toLowerCase();

			return listPriamychPlatieb.values().stream()
					.filter(platba -> platba.getZiadatel().toLowerCase().contains(normalizedFilter))
					.sorted((r1, r2) -> r2.getId().compareTo(r1.getId())).collect(Collectors.toList());
		} else {
			return new ArrayList(listPriamychPlatieb.values());
		}
	}

	public List<PoberatelSumar> findPoberatelov(String filter, int rokOd, int rokDo) {

		/*
		 * if (filter != null) { String normalizedFilter = filter.toLowerCase();
		 * 
		 * if (rokOd >= 2004 && rokOd <= 2017 && rokDo >= 2004 && rokDo <= 2017) {
		 * return listZiadatelov.values().stream() .filter(ziadatel ->
		 * filterTextOf(ziadatel).contains(normalizedFilter)) .sorted((r1, r2) ->
		 * r2.getMaxRozdielVymer(rokOd, rokDo) .compareTo(r1.getMaxRozdielVymer(rokOd,
		 * rokDo))) .collect(Collectors.toList()); } return new ArrayList<>(); } else {
		 * return new ArrayList(listZiadatelov.values()); }
		 */
		return null;
	}

	private void savePriamaPlatba(PriamaPlatba pp) {
		pp.setId(nextId.incrementAndGet());
		listPriamychPlatieb.put(pp.getId(), pp);
	}

	private String filterTextOf(PriamaPlatba priamaPlatba) {
		String filterableText = Stream.of(priamaPlatba.getZiadatel()).collect(Collectors.joining("\t"));
		return filterableText.toLowerCase();
	}
}
