package com.rebel.youtube.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rebel.youtube.model.DataGsonModel;
import com.rebel.youtube.model.PersonModel;
import com.rebel.youtube.repository.PersonRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api")
public class PersonApi extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		PersonRepository personRepository = new PersonRepository();
		Gson gson = new Gson();

		PrintWriter out;

		List<PersonModel> personModels = personRepository.read();
		try {
			out = response.getWriter();
			JsonObject json = new JsonObject();

			json.addProperty("status", true);
			json.addProperty("message", "list record");
			json.addProperty("data", gson.toJson(personModels));
			out.print(json.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		logger.log(Level.INFO, request.toString());

		String mode = request.getParameter("mode");
		String name = request.getParameter("name");
		int age = (request.getParameter("age") != null) ? Integer.valueOf(request.getParameter("age")) : 0;
		int personId = (request.getParameter("personId") != null) ? Integer.valueOf(request.getParameter("personId"))
				: 0;

		String search = request.getParameter("search");

		PersonRepository personRepository = new PersonRepository();
		Gson gson = new Gson();

		PrintWriter out;

		switch (mode) {
		case "create":
			int lastInsertId = personRepository.create(name, age);

			try {
				out = response.getWriter();
				JsonObject json = new JsonObject();

				json.addProperty("status", true);
				json.addProperty("message", "record created");
				json.addProperty("lastInsertId", String.valueOf(lastInsertId));

				out.print(json.toString());
			} catch (IOException e) {

				e.printStackTrace();
			}

			break;
		case "read":
			List<PersonModel> personModels = personRepository.read();
			try {
				out = response.getWriter();
				DataGsonModel dataGsonModel = new DataGsonModel();
				dataGsonModel.setStatus(true);
				dataGsonModel.setMessage("read record");
				dataGsonModel.setPersonModels(personModels);
				String jsonData = gson.toJson(dataGsonModel);
				out.print(jsonData);
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;
		case "search":
			List<PersonModel> personModelsSearch = personRepository.search(search);
			try {
				out = response.getWriter();
				DataGsonModel dataGsonModelSearch = new DataGsonModel();
				dataGsonModelSearch.setStatus(true);
				dataGsonModelSearch.setMessage("search record");
				dataGsonModelSearch.setPersonModels(personModelsSearch);
				String jsonData = gson.toJson(dataGsonModelSearch);
				out.print(jsonData);
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;
		case "update":
			personRepository.update(name, age, personId);
			try {
				out = response.getWriter();
				JsonObject json = new JsonObject();

				json.addProperty("status", true);
				json.addProperty("message", "record updated");

				out.print(json.toString());
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;
		case "delete":

			personRepository.delete(personId);
			try {
				out = response.getWriter();
				JsonObject json = new JsonObject();

				json.addProperty("status", true);
				json.addProperty("message", "record created");

				out.print(json.toString());
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;

		}
	}

}
