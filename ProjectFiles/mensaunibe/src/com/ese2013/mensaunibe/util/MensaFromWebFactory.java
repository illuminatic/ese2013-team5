package com.ese2013.mensaunibe.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.WeeklyPlan;
import com.ese2013.mensaunibe.util.database.MensaDatabase;

public class MensaFromWebFactory extends AbstractMensaFactory {
	
	private static WebService webService = new WebService();

	@Override
	public ArrayList<Mensa> createMensaList() throws WebLoadException {
		try {
			ArrayList<Mensa> mensas = createMensas();
			initializeMenuPlans(mensas);
			return mensas;
		} catch (Exception e) {
			throw new WebLoadException(e);
		}
	}

	private ArrayList<Mensa> createMensas() throws IOException, JSONException {
		MensaDatabase database = new MensaDatabase();
		database.open();
		ArrayList<Mensa> result = new ArrayList<Mensa>();
		JSONObject mensasJSON = webService.requestAllMensas();
		JSONArray array = mensasJSON.getJSONArray("mensas");
		for (int i = 0; i < array.length(); i++) {
			BuilderMensa mensaBuilder = new BuilderMensa(
					array.getJSONObject(i), database.isMensaFavorite(i));
			result.add(mensaBuilder.build());
		}
		return result;
	}

	private void initializeMenuPlans(List<Mensa> mensas) throws IOException, JSONException {
		for (int i = 0; i < mensas.size(); i++) {
			JSONObject menus = webService.requestMenusForMensa(mensas.get(i)
					.getId());
			JSONArray array = menus.getJSONArray("menus");
			mensas.get(i).setWeeklyPlan(new WeeklyPlan(array, mensas.get(i)));
		}
	}

}
