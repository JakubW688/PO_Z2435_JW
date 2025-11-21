import requests
from bs4 import BeautifulSoup
import os
import time
import plyer
import random
from urllib.parse import urljoin


NAZWA_PLIKU_ZAPISANYCH = "olx_zapisane_linki_elektronika.txt"
CZAS_OCZEKIWANIA = 600
LIMIT_OFERT_DO_MONITOROWANIA = 10

HEADERS = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}

SELEKTOR_KARTY = {'data-cy': 'l-card'}
URL_DO_MONITOROWANIA = "https://www.olx.pl/elektronika/?search%5Border%5D=created_at:desc"



def wczytaj_zapisane_linki():
    """Wczytuje unikalne linki z pliku do pamięci."""
    if not os.path.exists(NAZWA_PLIKU_ZAPISANYCH):
        return set()

    try:
        with open(NAZWA_PLIKU_ZAPISANYCH, 'r', encoding='utf-8') as f:
            return set(line.strip() for line in f)
    except Exception as e:
        print(f"Błąd podczas wczytywania pliku '{NAZWA_PLIKU_ZAPISANYCH}': {e}")
        return set()


def zapisz_nowy_link(link):
    """Dopisuje pojedynczy link na koniec pliku."""
    try:
        with open(NAZWA_PLIKU_ZAPISANYCH, 'a', encoding='utf-8') as f:
            f.write(link + "\n")
    except Exception as e:
        print(f"Błąd zapisu linku do pliku: {e}")


def wyslij_powiadomienie_systemowe(link):
    """Wysyła powiadomienie systemowe na pulpit."""
    try:
        plyer.notification.notify(
            title=' NOWA OFERTA OLX!',
            message=f'Kliknij, aby skopiować lub sprawdź konsolę.\n{link}',
            timeout=15,
            app_name='Monitor OLX'
        )
    except Exception as e:
        print(f"Błąd powiadomienia (sprawdź plyer): {e}")


def monitor_olx():
    print("--- START MONITORA OLX ---")
    zapisane_linki = wczytaj_zapisane_linki()
    print(f"Wczytano {len(zapisane_linki)} linków z historii.")
    print(f"Monitoruję adres: {URL_DO_MONITOROWANIA}")
    print("Naciśnij Ctrl+C, aby zakończyć.")

    try:
        while True:
            nowy_cykl_start = time.time()

            try:
                print(f"\n[{time.strftime('%H:%M:%S')}] Sprawdzam nowe oferty...")

                response = requests.get(URL_DO_MONITOROWANIA, headers=HEADERS, timeout=20)
                response.raise_for_status()

                soup = BeautifulSoup(response.text, 'html.parser')
                listings = soup.find_all(['div', 'article'], SELEKTOR_KARTY, limit=LIMIT_OFERT_DO_MONITOROWANIA)

                if not listings:
                    print(
                        "⚠️ Ostrzeżenie: Nie znaleziono ogłoszeń. Możliwa zmiana struktury strony OLX lub blokada tymczasowa.")

                nowe_oferty_znalezione = 0

                for listing in listings:
                    link_tag = listing.find('a')

                    # Bezpieczniejsze pobieranie href
                    relative_link = link_tag.get('href') if link_tag else None

                    if not relative_link:
                        continue

                    full_link = urljoin("https://www.olx.pl", relative_link)


                    full_link = full_link.split('#')[0]

                    if full_link not in zapisane_linki:
                        print("\n" + "=" * 40)
                        print(" NOWA OFERTA! ")
                        print(f"Link: {full_link}")
                        print("=" * 40)

                        wyslij_powiadomienie_systemowe(full_link)

                        zapisz_nowy_link(full_link)
                        zapisane_linki.add(full_link)
                        nowe_oferty_znalezione += 1


                        time.sleep(1)

                if nowe_oferty_znalezione == 0:
                    print("Brak nowych ofert.")

            except requests.exceptions.RequestException as e:
                print(f" Błąd połączenia: {e}")
            except Exception as e:
                print(f" Wystąpił nieoczekiwany błąd: {e}")


            losowe_odchylenie = random.randint(-60, 60)
            czas_do_spania = max(60, CZAS_OCZEKIWANIA + losowe_odchylenie)


            czas_przetwarzania = time.time() - nowy_cykl_start
            rzeczywisty_sen = max(0, czas_do_spania - czas_przetwarzania)

            print(
                f"Czekam {int(rzeczywisty_sen)} sekund (kolejne sprawdzenie ok. {time.strftime('%H:%M:%S', time.localtime(time.time() + rzeczywisty_sen))})...")
            time.sleep(rzeczywisty_sen)

    except KeyboardInterrupt:
        print("\nMonitorowanie zakończone przez użytkownika.")


if __name__ == "__main__":
    monitor_olx()