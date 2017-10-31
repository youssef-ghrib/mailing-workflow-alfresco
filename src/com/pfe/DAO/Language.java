package com.pfe.DAO;

import java.util.Locale;

import javax.faces.context.FacesContext;

public class Language {

	public static String getLanguage() {

		String lang = FacesContext.getCurrentInstance().getViewRoot()
				.getLocale().toString();

		return lang;
	}

	public static void setLanguage(String lang) {
		FacesContext.getCurrentInstance().getViewRoot()
				.setLocale(new Locale(lang));
	}

	public static String message(String key) {

		FacesContext context = FacesContext.getCurrentInstance();
		String value = context.getApplication().evaluateExpressionGet(context,
				"#{lang['" + key + "']}", String.class);

		return value;
	}

}
