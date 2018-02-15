package outils;

import java.io.File;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import application.MainApp;
import project.Project;

public class SauvegardeXML {

	/**
	 * Sauvegarde le project dans le XML donné
	 * 
	 * @param project
	 * @param file
	 */
	public static void saveToXML(Project project, File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(Project.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Marshalling and saving XML to the file.
			m.marshal(project, file);

			setFilePath(file);
		} catch (Exception e) { // catches ANY exception
			System.out.println("YALA ééé YA UNE ERREUR");
			e.printStackTrace();
		}
	}

	/**
	 * Charge le project à partir du XML donné
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Project loadFromXML(File file) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Project.class);
		Unmarshaller um = context.createUnmarshaller();

		setFilePath(file);

		return (Project) um.unmarshal(file);
	}

	// **********PATH
	/**
	 * Retourne le dernier fichier ouvert
	 */
	public static File getFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Enregistre le chemin du fichier dans le filePath
	 * @param file
	 */
	public static void setFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
		} else {
			prefs.remove("filePath");
		}
	}

}
