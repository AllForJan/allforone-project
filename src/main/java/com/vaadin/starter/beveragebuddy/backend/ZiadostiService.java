package com.vaadin.starter.beveragebuddy.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.starter.beveragebuddy.ui.converters.LocalDateToStringConverter;

public class ZiadostiService {

    private static class SingletonHolder {
        static final ZiadostiService INSTANCE = createDemoService();

        private SingletonHolder() {
        }

        private static ZiadostiService createDemoService() {
            final ZiadostiService service = new ZiadostiService();
            int rok;
            BigDecimal vymera;

            try {
                // URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera
                Scanner scanner = new Scanner(new File("c:/tmp/dataFIIT/ziadostiDiely.csv"), "UTF-8");
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
                            zd.setIco(-1);
                        } else {
                            zd.setIco(Integer.parseInt(fields[2]));
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
                            ziadatel.setZiadatel(zd.getZiadatel());
                        }

                        ziadatel.getListZiadostDiely().add(zd);

                        service.saveZiadatel(ziadatel);
                        service.saveZiadostDiely(zd);
                    } else {
                        System.err.println("Invalid record: " + line + " riadok >" + i);
                    }
                }
                scanner.close();
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();

            }
            nacitajPriamePlatby(service);
            return service;
        }

		private static void nacitajPriamePlatby(ZiadostiService service) {
			int i = 0;
			BufferedReader br = null;
			try {
				//URL;Meno;PSC;Obec;Opatrenie;Opatrenie - Kod;Suma;Rok
				br = new BufferedReader(new FileReader(new File("c:/tmp/dataFIIT/apa_prijimatelia_2018-03-15.csv")));
				String line;
				br.readLine();
				while ((line = br.readLine()) != null) {
					i++;
					line = line.replace("&quot;","").replace("&amp;","");
					String[] fields = line.split(";");
					if (fields.length >= 8) {
						PriamaPlatba pp = new PriamaPlatba();
						pp.setUrl(fields[0]);
						pp.setZiadatel(fields[1]);
						pp.setPsc(fields[2]);
						pp.setObec(fields[3]);
						pp.setOpatrenie(fields[4]);
						pp.setKodOpatrenia(fields[5]);
						String strPlatba = fields[6];
						try {
							pp.setSuma(new BigDecimal(strPlatba));
						}catch(NumberFormatException ex){
							pp.setSuma(new BigDecimal("0.0000000000001"));
							System.err.println("Invalid parsing bigdecimal: " + line + " riadok >" + i);
						}
						try {
							pp.setRok(Integer.parseInt(fields[7]));
						}catch(NumberFormatException ex){
							pp.setRok(-1);
							System.err.println("Invalid parsing of int: " + line + " riadok >" + i);
						}

						service.savePriamaPlatba(pp);

					} else {
						System.err.println("Invalid record: " + line + " riadok >" + i + " , " + fields.length);
					}
				}
				br.close();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
    }

    private Map<Long, ZiadostDiely> listZiadostDiely = new HashMap<>();
    private Map<Integer, Ziadatel> listZiadatelov = new HashMap<>();
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

    public Ziadatel findZiadatel(int ico) {

        return listZiadatelov.get(ico);
    }

    public List<Ziadatel> findZiadatelov(String filter) {

        if (filter != null) {
            String normalizedFilter = filter.toLowerCase();

            return listZiadatelov.values().stream()
                    .filter(ziadatel -> filterTextOf(ziadatel).contains(normalizedFilter))
                    .sorted((r1, r2) -> r2.getId().compareTo(r1.getId())).collect(Collectors.toList());
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
        String filterableText = Stream.of(ziadatel.getZiadatel(), ziadatel.getIco() + ""
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

	private void savePriamaPlatba(PriamaPlatba pp) {
		pp.setId(nextId.incrementAndGet());
		listPriamychPlatieb.put(pp.getId(), pp);
	}

	private String filterTextOf(PriamaPlatba priamaPlatba) {
		String filterableText = Stream.of(priamaPlatba.getZiadatel()
				// String.valueOf(review.getScore()),
				// String.valueOf(review.getCount()),
		).collect(Collectors.joining("\t"));
		return filterableText.toLowerCase();
	}
}
