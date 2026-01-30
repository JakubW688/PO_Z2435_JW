import requests
from bs4 import BeautifulSoup
import time
import os
import logging
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

PLIK_BAZA = "historia_ofert.txt"
URL_OLX = "https://www.olx.pl/elektronika/?search%5Border%5D=created_at:desc"
URL_ALLEGRO = "https://allegro.pl/kategoria/elektronika?order=n"
CZAS_SPRAWDZANIA = 300

EMAIL_NADAWCA = "projektaplikacyjnetest@gmail.com"
EMAIL_HASLO = "eekk rnog jpel znfu"
EMAIL_ODBIORCA = "dzik7331@wp.pl"
SMTP_SERVER = "smtp.gmail.com"
SMTP_PORT = 465

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)


def wczytaj_historie():
    if not os.path.exists(PLIK_BAZA):
        return set()
    try:
        with open(PLIK_BAZA, 'r', encoding='utf-8') as f:
            return set(line.strip() for line in f)
    except IOError as e:
        logging.error(f"Błąd odczytu bazy: {e}")
        return set()


def zapisz_link_do_historii(link):
    try:
        with open(PLIK_BAZA, 'a', encoding='utf-8') as f:
            f.write(link + "\n")
    except IOError as e:
        logging.error(f"Błąd zapisu do bazy: {e}")


def wyslij_email(tytul, link, zrodlo):
    try:
        msg = MIMEMultipart()
        msg['From'] = EMAIL_NADAWCA
        msg['To'] = EMAIL_ODBIORCA
        msg['Subject'] = f"[{zrodlo}] Nowa okazja: {tytul}"

        tresc = f"""
        Witam!

        Znalazłem nową ofertę w serwisie {zrodlo}:
        Tytuł: {tytul}

        Link do oferty:
        {link}

        Pozdrawiam,
        Twój Bot
        """

        msg.attach(MIMEText(tresc, 'plain', 'utf-8'))

        with smtplib.SMTP_SSL(SMTP_SERVER, SMTP_PORT) as server:
            server.login(EMAIL_NADAWCA, EMAIL_HASLO)
            server.send_message(msg)

        logging.info(f"📧 Wysłano email o ofercie: {tytul}")

    except Exception as e:
        logging.error(f"❌ Błąd wysyłania maila: {e}")


def wyslij_powiadomienie(tytul, link, zrodlo):
    logging.info(f"📢 ZNALAZŁEM ({zrodlo}): {tytul}")
    wyslij_email(tytul, link, zrodlo)


def parsuj_olx():
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
    }

    try:
        response = requests.get(URL_OLX, headers=headers, timeout=20)
        response.raise_for_status()
        soup = BeautifulSoup(response.text, 'html.parser')

        karty = soup.select('[data-cy="l-card"]')
        znalezione = []

        for karta in karty:
            link_tag = karta.find('a')
            if not link_tag: continue

            href = link_tag.get('href')

            tytul_tag = karta.find('h6')
            tytul = tytul_tag.get_text(strip=True) if tytul_tag else "Nowa oferta OLX"

            if not href: continue

            full_link = "https://www.olx.pl" + href if href.startswith('/') else href
            znalezione.append((tytul, full_link, "OLX"))

        return znalezione

    except Exception as e:
        logging.error(f"Błąd parsowania OLX: {e}")
        return []


def parsuj_allegro():
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8',
        'Accept-Language': 'pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7'
    }

    try:
        response = requests.get(URL_ALLEGRO, headers=headers, timeout=20)

        if response.status_code == 403:
            logging.warning("Allegro zablokowało dostęp (Błąd 403).")
            return []

        response.raise_for_status()
        soup = BeautifulSoup(response.text, 'html.parser')

        artykuly = soup.find_all('article')
        znalezione = []

        for art in artykuly:
            header = art.find('h2')
            if not header:
                continue

            link_tag = header.find('a')
            if not link_tag:
                continue

            tytul = link_tag.get_text(strip=True)
            href = link_tag.get('href')

            if not href: continue

            full_link = href
            if href.startswith('/'):
                full_link = "https://allegro.pl" + href

            znalezione.append((tytul, full_link, "Allegro"))

        return znalezione

    except Exception as e:
        logging.error(f"Błąd parsowania Allegro: {e}")
        return []


def main():
    logging.info("--- START PROGRAMU ---")

    historia = wczytaj_historie()
    logging.info(f"Baza zawiera {len(historia)} linków.")

    try:
        while True:
            oferty_olx = parsuj_olx()
            oferty_allegro = parsuj_allegro()

            wszystkie_oferty = oferty_olx + oferty_allegro

            nowe = 0
            for tytul, link, zrodlo in wszystkie_oferty:
                if link not in historia:
                    wyslij_powiadomienie(tytul, link, zrodlo)
                    zapisz_link_do_historii(link)
                    historia.add(link)
                    nowe += 1
                    time.sleep(2)

            if nowe == 0:
                logging.info("Brak nowych ofert.")

            logging.info(f"Czekam {CZAS_SPRAWDZANIA}s...")
            time.sleep(CZAS_SPRAWDZANIA)

    except KeyboardInterrupt:
        logging.info("Koniec pracy.")


if __name__ == "__main__":
    main()