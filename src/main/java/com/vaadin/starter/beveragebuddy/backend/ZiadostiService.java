package com.vaadin.starter.beveragebuddy.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class ZiadostiService {

	private static class SingletonHolder {
		static final ZiadostiService INSTANCE = createDemoService();

		private SingletonHolder() {
		}

		private static ZiadostiService createDemoService() {
			// Load FinStat data
			Map<String, FinstatData> rows = new HashMap<>();
			boolean firstLine = true;
			int lineNr = 0;
			try (Scanner finstatScanner = new Scanner(new File("data/finstat.csv"), "UTF-8")) {
				while (finstatScanner.hasNextLine()) {
					if (firstLine) {
						finstatScanner.nextLine();
						firstLine = false;
						continue;
					}
					String line = finstatScanner.nextLine();
					line = line.concat("xxx;");
					String[] items = line.split(";");

					if (items.length == 1) {
						continue;
					}

					FinstatData row = new FinstatData();
					row.setIco(items[0]);
					row.setNazov(items[1]);
					row.setHlavnaCinnost(items[2]);
					row.setSkNace(items[3]);
					// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					// row.setDatumVzniku(formatter.);
					// row.setDatumZaniku(DateFormat.getDateInstance().parse(items[5]));
					row.setDlhy(items[6]);
					row.setZamestnanciStat(items[7]);
					row.setZamestnanciPocet(StringUtils.isNotEmpty(items[8]) ? Integer.parseInt(items[8]) : -1);
					row.setAdresa(items[9]);
					row.setPsc(items[10]);
					row.setMesto(items[11]);
					row.setOkres(items[12]);
					row.setKraj(items[13]);
					// items[14] & 15 to 19 not used (year 2018)
					int year = 2017;
					for (int i = 20, idx = 0; i < items.length - 1; i = i + 5) {
						FinstatYearlyData yearData = new FinstatYearlyData();
						yearData.setRok(year - idx++);
						yearData.setTrzby(StringUtils.isNotEmpty(items[i]) ? Double.parseDouble(items[i]) : Double.NaN);
						yearData.setVynosy(StringUtils.isNotEmpty(items[i + 1]) ? Double.parseDouble(items[i + 1])
								: Double.NaN - yearData.getTrzby());
						yearData.setZisk(
								StringUtils.isNotEmpty(items[i + 2]) ? Double.parseDouble(items[i + 2]) : Double.NaN);
						yearData.setAktiva(
								StringUtils.isNotEmpty(items[i + 3]) ? Double.parseDouble(items[i + 3]) : Double.NaN);
						yearData.setZamestnanciStat(items[i + 4]);
						row.getRocneData().add(yearData);
					}
					rows.put(row.getIco(), row);
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			final ZiadostiService service = new ZiadostiService();
			int rok;
			BigDecimal vymera;

			try (Scanner scanner = new Scanner(new File("c:/tmp/dataFIIT/ziadostiDiely.csv"), "UTF-8")) {
				// URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera
				// scanner.useDelimiter("\n");

				// while (scanner.hasNext()) {
				// System.out.println(scanner.nextLine());
				// }
				int i = 0;
				while (scanner.hasNextLine()) {
					i++;
					String line = scanner.nextLine();
					String[] fields = line.split(";");
					if (fields.length >= 8) {
						ZiadostDiely zd = new ZiadostDiely();
						zd.setUrl(fields[0]);
						zd.setZiadatel(fields[1]);
						if (fields[2].equals("")) {
							// ak ico neexistuje, daj tam meno osoby
							zd.setIco(fields[1]);
						} else {
							zd.setIco(fields[2]);
						}
						zd.setRok(Integer.parseInt(fields[3]));
						zd.setLokalita(fields[4]);
						zd.setDiel(fields[5]);
						zd.setKultura(fields[6]);
						String strVymera = fields[7].replace(" ha", "");
						try {
							zd.setVymera(new BigDecimal(strVymera));
						} catch (NumberFormatException ex) {
							zd.setVymera(new BigDecimal("0.0000000000001"));
						}

						Ziadatel ziadatel = service.findZiadatel(zd.getIco());
						if (ziadatel == null) {
							ziadatel = new Ziadatel();
							ziadatel.setIco(zd.getIco());
							ziadatel.setFinstatData(rows.get(ziadatel.getIco()));
						}
						ziadatel.setZiadatel(zd.getZiadatel());
						ziadatel.addZiadostDiely(zd);

						service.saveZiadatel(ziadatel);
						service.saveZiadostDiely(zd);
					} else {
						System.err.println("Invalid record: " + line + " riadok >" + i);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();

			}

			nacitajPriamePlatby(service);
			return service;
		}

		private static void nacitajPriamePlatby(ZiadostiService service) {
			int i = 0;
			try {
				// URL;Meno;PSC;Obec;Opatrenie;Opatrenie - Kod;Suma;Rok
				Scanner scanner = new Scanner(new File("c:/tmp/dataFIIT/apa_prijimatelia_2018-03-15.csv"), "UTF-8");

				String line;
				scanner.nextLine();
				while (scanner.hasNext()) {
					i++;
					line = scanner.nextLine().replace("&quot;", "").replace("&amp;", "");
					String[] fields = line.split(";");
					if (fields.length >= 8) {
						PriamaPlatba pp = new PriamaPlatba();
						pp.setUrl(fields[0]);
						pp.setZiadatel(fields[1]);
						String psc = fields[2].replace(" ", "");
						pp.setPsc(StringUtils.leftPad(psc, 5, "0"));
						pp.setObec(fields[3]);
						pp.setOpatrenie(fields[4]);
						pp.setKodOpatrenia(fields[5]);
						String strPlatba = fields[6];
						try {
							pp.setSuma(new BigDecimal(strPlatba));
						} catch (NumberFormatException ex) {
							pp.setSuma(new BigDecimal("0.0000000000001"));
							System.err.println("Invalid parsing bigdecimal: " + line + " riadok >" + i);
						}
						try {
							pp.setRok(Integer.parseInt(fields[7]));
						} catch (NumberFormatException ex) {
							pp.setRok(-1);
							System.err.println("Invalid parsing of int: " + line + " riadok >" + i);
						}
						service.savePriamaPlatba(pp);
					} else {
						System.err.println("Invalid record: " + line + " riadok >" + i + " , " + fields.length);
					}
				}
				scanner.close();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}

		}
	}

	private Map<Long, ZiadostDiely> listZiadostDiely = new HashMap<>();
	private Map<String, Ziadatel> listZiadatelov = new HashMap<>();
	private Map<Long, PriamaPlatba> listPriamychPlatieb = new HashMap<>();

	private AtomicLong nextId = new AtomicLong(0);

	private ZiadostiService() {
	}

	public static ZiadostiService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public List<ZiadostDiely> findZiadostDiely(String filter) {

		return new ArrayList(listZiadostDiely.values());// .subList(0, 1000);
	}
	
	public Ziadatel findZiadatel(String ico) {

		return listZiadatelov.get(ico);
	}

	public List<Ziadatel> findZiadatelov(String filter, int rokOd, int rokDo) {

		if (filter != null) {
			String normalizedFilter = filter.toLowerCase();

			if (rokOd >= 2004 && rokOd <= 2017 && rokDo >= 2004 && rokDo <= 2017) {
				return listZiadatelov.values().stream()
						.filter(ziadatel -> filterTextOf(ziadatel).contains(normalizedFilter))
						.sorted((r1, r2) -> r2.getMaxRozdielVymer(rokOd, rokDo)
								.compareTo(r1.getMaxRozdielVymer(rokOd, rokDo)))
						.collect(Collectors.toList());
			}
			return new ArrayList<>();
		} else {
			return new ArrayList(listZiadatelov.values());
		}
	}

	public boolean deleteReview(ZiadostDiely dto) {
		return listZiadostDiely.remove(dto.getId()) != null;
	}

	public void saveZiadostDiely(ZiadostDiely dto) {
		ZiadostDiely entity = listZiadostDiely.get(dto.getId());

		if (entity == null) {
			entity = dto;
			if (dto.getId() == null) {
				entity.setId(nextId.incrementAndGet());
			}
			listZiadostDiely.put(entity.getId(), entity);
			// System.out.println("ok " + entity.getId());
		} else {
			System.out.println("+++++++++++++" + entity.getId());
		}
	}

	public void saveZiadatel(Ziadatel dto) {
		Ziadatel entity = listZiadatelov.get(dto.getIco());

		if (entity == null) {
			entity = dto;
			if (dto.getId() == null) {
				entity.setId(nextId.incrementAndGet());
			}
			listZiadatelov.put(entity.getIco(), entity);

		}
	}

	private String filterTextOf(Ziadatel ziadatel) {
		String filterableText = Stream.of(ziadatel.getZiadatel(), ziadatel.getIco()
		// String.valueOf(review.getScore()),
		// String.valueOf(review.getCount()),
		).collect(Collectors.joining("\t"));
		return filterableText.toLowerCase();
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

	public List<PriamaPlatba> findPriamaPlatba(Ziadatel ziadatel) {
		Set<String> mena = new HashSet<>();
		mena.add(ziadatel.getZiadatel());
		mena.addAll(ziadatel.getDalsieNazvy());

 		return listPriamychPlatieb.values().stream()
				.filter(platba -> mena.contains(platba.getZiadatel())
						&& platba.getPsc().replace(" ", "").equals(ziadatel.getFinstatData().getPsc().replace(" ", "")))
				.sorted((r1, r2) -> r2.getId().compareTo(r1.getId())).collect(Collectors.toList());

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
