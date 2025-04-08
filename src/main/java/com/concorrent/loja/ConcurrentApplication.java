package com.concorrent.loja;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.*;

public class ConcurrentApplication {

    private static final int THREADS = 200;
    private static final int REQUESTS_PER_THREAD = 200;
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final Random random = new Random();

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS, runnable -> {
            Thread t = new Thread(runnable);
            t.setName("User-" + t.getId());
            return t;
        });

        for (int i = 0; i < THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    try {
                        int action = random.nextInt(4);
                        switch (action) {
                            case 0 -> getProducts();
                            case 1 -> purchaseProduct();
                            case 2 -> updateProduct();
                            case 3 -> generateReport();
                        }
                        Thread.sleep(random.nextInt(100));
                    } catch (Exception e) {
                        System.err.printf("Erro na thread [%s]: %s%n",
                                Thread.currentThread().getName(), e);
                        e.printStackTrace();
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("Teste de carga concluído.");
    }

    private static void getProducts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "/products"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log("GET /products", response);
    }

    private static void purchaseProduct() throws Exception {
        int productId = 1;
        int qty = random.nextInt(3) + 1;

        String json = String.format("{\"id\":%d,\"quantity\":%d}", productId, qty);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/products/purchase"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log("POST /products/purchase", response);
    }

    private static void updateProduct() throws Exception {
        int productId = 1;
        int qty = random.nextInt(50) + 1;

        String json = String.format("{\"quantity\":%d}", qty);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/products/" + productId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log("PUT /products/" + productId, response);
    }

    private static void generateReport() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "/products/report"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log("GET /products/report", response);
    }

    private static void log(String action, HttpResponse<String> response) {
        System.out.printf("[%s] %s | status=%d | body=%s%n",
                Thread.currentThread().getName(),
                action,
                response.statusCode(),
                truncate(response.body(), 200));
    }

    private static String truncate(String str, int maxLen) {
        return str.length() > maxLen ? str.substring(0, maxLen) + "..." : str;
    }
}
