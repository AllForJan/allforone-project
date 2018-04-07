package com.vaadin.starter.beveragebuddy.backend.rpvs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.starter.beveragebuddy.backend.Ziadatel;
import com.vaadin.starter.beveragebuddy.backend.ZiadostiService;

public class RpvsService {

	private static class SingletonHolder {
		static final RpvsService INSTANCE = createDemoService();

		private SingletonHolder() {
		}

		private static RpvsService createDemoService() {
			final RpvsService service = new RpvsService();

			ZiadostiService.getInstance();

			Map<String, List<PoberatelVyhod>> json = new HashMap<>();
			final ObjectMapper mapper = new ObjectMapper();
			try {
				System.out.println("Loading RPVS data");
				json = mapper.readValue(new File("data/rpvs_2018-04-07.json"),
						new TypeReference<Map<String, List<PoberatelVyhod>>>() {
						});
			} catch (IOException e) {
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

			// List<PoberatelSumar> poberateliaCelkovo = new ArrayList<>();
			System.out.println("Normalizing RPVS data");
			for (PoberatelVyhod poberatel : poberateloveFirmy.keySet()) {
				PoberatelSumar sumar = new PoberatelSumar();
				sumar.setPoberatel(poberatel);
				for (String ico : poberateloveFirmy.get(poberatel)) {
					FirmaPlatby platbyFirme = new FirmaPlatby();
					Ziadatel ziadatel = ZiadostiService.getInstance().findZiadatel(ico);
					if (ziadatel == null) {
						continue;
					}

					platbyFirme.getPriamePlatby().addAll(ZiadostiService.getInstance().findPriamaPlatba(ziadatel));
					platbyFirme.setIco(ico);
					platbyFirme.setNazov(ziadatel.getZiadatel());
					sumar.getPlatbyFirmam().add(platbyFirme);
				}
				sumar.initializeSumaVsetkychPlatieb();
				service.getPoberateliaSumar().add(sumar);
			}

			System.out.println("RPVS done");
/*			service.getPoberateliaSumar().sort(Comparator.comparing(PoberatelSumar::getSumaVsetkychPlatieb));

			try (Writer out = new OutputStreamWriter(new FileOutputStream("data/poberatelia.txt"), "UTF8")) {
				for (PoberatelSumar sumar : service.getPoberateliaSumar()) {
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
*/
			return service;
		}
	}

	private List<PoberatelSumar> poberateliaSumar = new ArrayList<>();

	// private AtomicLong nextId = new AtomicLong(0);

	private RpvsService() {
	}

	public static RpvsService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public List<PoberatelSumar> getPoberateliaSumar() {
		return this.poberateliaSumar;
	}

	public List<PoberatelSumar> findPoberatelov(String filter, int rokOd, int rokDo) {
		if (filter != null) {
			String normalizedFilter = filter.toLowerCase();

			if (rokOd >= 2004 && rokOd <= 2017 && rokDo >= 2004 && rokDo <= 2017) {
				this.poberateliaSumar.stream()
						.filter(poberatelSumar -> filterTextOf(poberatelSumar).contains(normalizedFilter))
						.sorted((r1, r2) -> r2.getSumaVsetkychPlatieb().compareTo(r1.getSumaVsetkychPlatieb()))
						.collect(Collectors.toList());
			}
			return new ArrayList<>();
		} else {
			return Collections.unmodifiableList(this.poberateliaSumar);
		}
	}

	// private void savePriamaPlatba(PriamaPlatba pp) {
	// pp.setId(nextId.incrementAndGet());
	// listPriamychPlatieb.put(pp.getId(), pp);
	// }
	private String filterTextOf(PoberatelSumar poberatelSumar) {
		String filterableText = Stream
				.of(poberatelSumar.getPoberatel().getName(), poberatelSumar.getPoberatel().getStreet(),
						poberatelSumar.getPoberatel().getCity(), poberatelSumar.getPoberatel().getZipCode())
				.collect(Collectors.joining("\t"));
		return filterableText.toLowerCase();
	}

	// private String filterTextOf(PriamaPlatba priamaPlatba) {
	// String filterableText =
	// Stream.of(priamaPlatba.getZiadatel()).collect(Collectors.joining("\t"));
	// return filterableText.toLowerCase();
	// }
}
