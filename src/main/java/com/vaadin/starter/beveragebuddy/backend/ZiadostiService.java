package com.vaadin.starter.beveragebuddy.backend;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
						zd.setIco(fields[2]);
						zd.setRok(Integer.parseInt(fields[3]));
						zd.setLokalita(fields[4]);
						zd.setDiel(fields[5]);
						zd.setKultura(fields[6]);
						String strVymera = fields[7].replace(" ha","");
						try {
							zd.setVymera(new BigDecimal(strVymera));
						}catch(NumberFormatException ex){
							zd.setVymera(new BigDecimal("0.0000000000001"));
						}


						Ziadatel ziadatel = service.findZiadatel(zd.getIco());
						if (ziadatel == null) {
							ziadatel = new Ziadatel();
							ziadatel.setIco(zd.getIco());
							ziadatel.setZiadatel(zd.getZiadatel());
							for (ZiadostDiely diely : ziadatel.getListZiadostDiely()) {
								vymera = ziadatel.getVymera(diely.getRok());
								vymera = vymera.add(diely.getVymera());
								ziadatel.setVymera(diely.getRok(),vymera);
							}
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
			return service;
		}
	}

	private Map<Long, ZiadostDiely> listZiadostDiely = new HashMap<>();
	private Map<String, Ziadatel> listZiadatelov = new HashMap<>();

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
		String filterableText = Stream.of(ziadatel.getZiadatel(), ziadatel.getIco()
		// String.valueOf(review.getScore()),
		// String.valueOf(review.getCount()),
		).collect(Collectors.joining("\t"));
		return filterableText.toLowerCase();
	}
}
