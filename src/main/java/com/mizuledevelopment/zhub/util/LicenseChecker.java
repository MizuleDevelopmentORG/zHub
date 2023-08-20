package com.mizuledevelopment.zhub.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mizuledevelopment.zhub.zHub;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LicenseChecker {

    private static final String LICENSE_STRING = "https://api.mizuledevelopment.com/license/validate";
    private static final URI LICENSE_URI = URI.create(LICENSE_STRING);

    private final zHub zhub;
    private final String plugin;

    public LicenseChecker(final String plugin) {
        this.zhub = zHub.instance();
        this.plugin = plugin;
    }

    public boolean check() {
        final String license = this.zhub.getConfig().getString("license");

        if (license == null || license.trim().isBlank()) {
            this.zhub.getSLF4JLogger().error("No license found! Please add your license to the config.yml");
            this.zhub.getServer().getPluginManager().disablePlugin(this.zhub);
            return false;
        }

        final HttpClient client = HttpClient.newHttpClient();

        final JsonObject obj = new JsonObject();

        obj.addProperty("key", license);
        obj.addProperty("plugin", this.plugin);

        final HttpResponse<String> response;
        try {
            response = client.send(HttpRequest.newBuilder()
                    .uri(LICENSE_URI)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(obj)))
                    .build(), HttpResponse.BodyHandlers.ofString());
        } catch (final IOException | InterruptedException e) {
            this.zhub.getSLF4JLogger().error("An error occurred while validating your license. ({})", e.getMessage(), e);
            this.zhub.getServer().getPluginManager().disablePlugin(this.zhub);
            return false;
        }

        final JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
        if (response.statusCode() == 202) {
            this.zhub.getSLF4JLogger().info("LICENSE VALID!");
            this.zhub.getSLF4JLogger().info("Welcome " + json.get("owner").getAsString());
            return true;
        } else if (response.statusCode() == 402) {
            this.zhub.getSLF4JLogger().error("Your license is invalid. ({})", json.get("message").getAsString());
            return false;
        } else if (response.statusCode() == 403) {
            this.zhub.getSLF4JLogger().error("Your license is valid, however you have reached the address limit. ({})", json.get("message").getAsString());
            this.zhub.getServer().getPluginManager().disablePlugin(this.zhub);
            return false;
        } else {
            this.zhub.getSLF4JLogger().error("An error occurred while validating your license. ({})", json.get("message").getAsString());
            this.zhub.getServer().getPluginManager().disablePlugin(this.zhub);
            return false;
        }
    }


}
