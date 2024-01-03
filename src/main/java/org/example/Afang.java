package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.stream.Stream;

public class Afang {
    public static void main(String[] args) throws IOException {
        String dar = "<html><head><title>Mear</title></head><body><p>This is a text</p></body></html>";

        Document me =Jsoup.parse(dar);
        String der = "<div><p> Hello World </p></div>";
        Document mer = Jsoup.parseBodyFragment(der);
        Element body = mer.body();

        Document dde = Jsoup.connect("https://jsoup.org/cookbook/input/load-document-from-url").get();
        String title = dde.title();
        System.out.println(title);

    }

}
