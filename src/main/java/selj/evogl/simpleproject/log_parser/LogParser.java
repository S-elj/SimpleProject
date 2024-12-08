package selj.evogl.simpleproject.log_parser;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.*;

public class LogParser {

    private Map<String, UserProfile> userProfiles = new HashMap<>();

    public void parseLogs(String logFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(logFilePath));
        String line;

        while ((line = br.readLine()) != null) {
            // Extraire la portion JSON en cherchant le premier '{'
            int jsonStartIndex = line.indexOf('{');
            if (jsonStartIndex != -1) {
                String jsonString = line.substring(jsonStartIndex);

                // Vérification rapide de la validité du JSON avant d'essayer de le parser
                if (isValidJson(jsonString)) {
                    try {
                        JSONObject logEntry = new JSONObject(jsonString);

                        // Extraire les données de l'utilisateur depuis l'entrée JSON
                        JSONObject userJson = logEntry.getJSONObject("user");
                        String userId = userJson.getString("id");
                        String userName = userJson.getString("name");
                        String userMail = userJson.getString("email");
                        int userAge = userJson.getInt("age");

                        // Vérifier si l'utilisateur existe déjà dans la collection
                        UserProfile profile = userProfiles.get(userId);
                        if (profile == null) {
                            // Si l'utilisateur n'existe pas encore, on crée un nouveau profil
                            profile = new UserProfile(userId, userName, userMail, userAge);
                            userProfiles.put(userId, profile);
                        }

                        // Incrémenter les opérations de lecture ou d'écriture
                        String operation = logEntry.getString("operation");
                        profile.incrementOperation(operation);

                        // Ajouter les produits recherchés (si présents)
                        if (logEntry.has("product_name")) {
                            String productName = logEntry.getString("product_name");
                            double productPrice = logEntry.getDouble("product_price");
                            profile.addProductSearched(productName, productPrice);
                        }

                    } catch (Exception e) {
                        System.err.println("Erreur de parsing pour la ligne : " + line);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("JSON invalide dans la ligne : " + line);
                }
            } else {
                System.err.println("Aucun JSON trouvé dans la ligne : " + line);
            }
        }
        br.close();
    }


    public void saveProfilesToJson(String outputPath) throws IOException {
        // Créer un FileWriter pour écrire dans le fichier
        FileWriter writer = new FileWriter(outputPath);
        writer.write("{\n");

        // Vérifier si la collection userProfiles n'est pas vide
        if (userProfiles.isEmpty()) {
            System.err.println("Aucun profil utilisateur trouvé. Le fichier JSON sera vide.");
            writer.write("{}\n");
            writer.close();
            return;
        }

        boolean first = true;
        for (UserProfile profile : userProfiles.values()) {
            // Si ce n'est pas le premier profil, on ajoute une virgule pour séparer les éléments
            if (!first) {
                writer.write(",\n");
            }
            first = false;
            System.out.println("Ecriture d'un profile json: " +profile.getUserName());

            // Écrire le profil de l'utilisateur dans le fichier
            writer.write("\"" + profile.getUserId() + "\": {\n");
            writer.write("\"userId\": \"" + profile.getUserId() + "\",\n");
            writer.write("\"userName\": \"" + profile.getUserName() + "\",\n");
            writer.write("\"userMail\": \"" + profile.getUserMail() + "\",\n");
            writer.write("\"userAge\": \"" + profile.getUserAge() + "\",\n");
            writer.write("\"readOperations\": " + profile.getReadOperations() + ",\n");
            writer.write("\"writeOperations\": " + profile.getWriteOperations() + ",\n");
            writer.write("\"Most_expensive_product_cost\": " + profile.getMostExpensiveProductPrice() + ",\n");




            writer.write("\"productsSearched\": [\n");

            // Vérifier s'il y a des produits recherchés
            if (profile.getProductsSearched().isEmpty()) {
                writer.write("]\n"); // Pas de produit, fermer immédiatement
            } else {
                boolean firstProduct = true;
                for (Map.Entry<String, Double> entry : profile.getProductsSearched().entrySet()) {
                    if (!firstProduct) {
                        writer.write(",\n");
                    }
                    firstProduct = false;
                    writer.write("{ \"productName\": \"" + entry.getKey() + "\", \"productPrice\": " + entry.getValue() + " }");
                }
                writer.write("\n]");
            }

            writer.write("\n}"); // Fermer le profil de l'utilisateur
        }

        writer.write("\n}\n");
        writer.close();
    }

    private boolean isValidJson(String jsonString) {
        try {
            new JSONObject(jsonString);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

}
