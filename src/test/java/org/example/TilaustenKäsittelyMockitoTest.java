package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

public class TilaustenKäsittelyMockitoTest {
    @Mock
    IHinnoittelija hinnoittelijaMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testaaKäsittelijäWithMockitoHinnoittelija() {
        float alkuSaldo = 100.0f;
        float listaHinta = 30.0f;
        float alennus = 20.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("TDD in Action", listaHinta);
        when(hinnoittelijaMock.getAlennusProsentti(any(Asiakas.class), any(Tuote.class))).thenReturn(alennus);
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));
        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(any(Asiakas.class), any(Tuote.class));
    }

    @Test
    public void testaaAloitaMetodia() {
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(new Asiakas(100.0f), new Tuote("Tuote", 50.0f)));
        verify(hinnoittelijaMock, times(1)).aloita();
    }

    @Test
    public void testaaSetAlennusProsenttiMetodia() {
        Asiakas asiakas = new Asiakas(100.0f);
        float alennusProsentti = 15.0f;
        hinnoittelijaMock.setAlennusProsentti(asiakas, alennusProsentti);
        verify(hinnoittelijaMock).setAlennusProsentti(asiakas, alennusProsentti);
    }

    @Test
    public void testaaLopetaMetodia() {
        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(new Asiakas(100.0f), new Tuote("Tuote", 50.0f)));
        verify(hinnoittelijaMock, times(1)).lopeta();
    }

    @Test
    public void testaaKäsittelyAlle100() {
        float alkuSaldo = 200.0f;
        float listaHinta = 80.0f; // Hinta alle 100
        float alennus = 10.0f; // Oletettu alennus
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("Java Basics", listaHinta);

        when(hinnoittelijaMock.getAlennusProsentti(any(Asiakas.class), any(Tuote.class))).thenReturn(alennus);

        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));

        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(any(Asiakas.class), any(Tuote.class));
    }

    @Test
    public void testaaKäsittelyYliTaiYhtäSuuriKuin100() {
        float alkuSaldo = 300.0f;
        float listaHinta = 150.0f; // Hinta yli 100
        float alennus = 20.0f; // Oletettu alennus
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("Advanced Java", listaHinta);

        when(hinnoittelijaMock.getAlennusProsentti(any(Asiakas.class), any(Tuote.class))).thenReturn(alennus);

        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));

        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, times(2)).getAlennusProsentti(any(Asiakas.class), any(Tuote.class));
    }
}
