import requests
from bs4 import BeautifulSoup
import os
import time
import plyer


NAZWA_PLIKU_ZAPISANYCH = "olx_zapisane_linki_elektronika.txt"
CZAS_OCZEKIWANIA = 600
LIMIT_OFERT_DO_MONITOROWANIA = 10  # NOWY LIMIT: Skanujemy tylko pierwszych 5 ogłoszeń na stronie.

HEADERS = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}

# Selektor karty (ustalony na podstawie Twoich zrzutów)
SELEKTOR_KARTY = {'data-cy': 'l-card'}
# Adres URL do monitorowania (Elektronika)
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
    with open(NAZWA_PLIKU_ZAPISANYCH, 'a', encoding='utf-8') as f:
        f.write(link + "\n")


def wyslij_powiadomienie_systemowe(link):
    """Wysyła powiadomienie systemowe na pulpit."""
    try:
        plyer.notification.notify(
            title='🔔 NOWA OFERTA OLX - ELEKTRONIKA!',
            message=f'Link: {link}',
            timeout=15,
            app_name='Monitor OLX'
        )
    except Exception as e:
        print(f"Błąd podczas wysyłania powiadomienia (sprawdź instalację plyer): {e}")


def monitor_olx():
    zapisane_linki = wczytaj_zapisane_linki()
    print(f"Wczytano {len(zapisane_linki)} linków.")
    print(f"Monitoruję: {URL_DO_MONITOROWANIA} (Limit: {LIMIT_OFERT_DO_MONITOROWANIA} pierwszych ogłoszeń)")

    try:
        # Główna pętla monitorowania
        while True:
            nowy_cykl_start = time.time()

            try:
                print(f"\n[{time.strftime('%H:%M:%S')}] Sprawdzam nowe oferty...")

                response = requests.get(URL_DO_MONITOROWANIA, headers=HEADERS, timeout=15)
                response.raise_for_status()

                soup = BeautifulSoup(response.text, 'html.parser')

                # Użycie ustalonego selektora karty
                # POBIERAMY TYLKO PIERWSZE 5 WYNIKÓW
                listings = soup.find_all(['div', 'article'], SELEKTOR_KARTY, limit=LIMIT_OFERT_DO_MONITOROWANIA)

                if not listings:
                    print("Brak wyników lub błąd selektora karty. Upewnij się, że SELEKTOR_KARTY jest poprawny.")
                    time.sleep(CZAS_OCZEKIWANIA)
                    continue

                nowe_oferty_znalezione = 0

                for listing in listings:

                    link_tag = listing.find('a')
                    relative_link = link_tag['href'] if link_tag and link_tag.has_attr('href') else None

                    if not relative_link:
                        continue

                    if relative_link.startswith('/'):
                        full_link = f"https://www.olx.pl{relative_link}"
                    else:
                        full_link = relative_link

                    if full_link not in zapisane_linki:
                        print("\n🔔🔔🔔 NOWA OFERTA! 🔔🔔🔔")
                        print(f"Link: {full_link}")

                        wyslij_powiadomienie_systemowe(full_link)

                        zapisz_nowy_link(full_link)
                        zapisane_linki.add(full_link)
                        nowe_oferty_znalezione += 1
                        time.sleep(1)

                if nowe_oferty_znalezione == 0:
                    print("Brak nowych ofert. Wszystkie są już znane.")

                print(f"Łączna liczba zapisanych linków w bazie: {len(zapisane_linki)}")

            except requests.exceptions.RequestException as e:
                print(f"Błąd połączenia: {e}")
            except Exception as e:
                print(f"Wystąpił nieoczekiwany błąd: {e}")


            czas_trwania_cyklu = time.time() - nowy_cykl_start
            czas_snu = CZAS_OCZEKIWANIA - czas_trwania_cyklu
            if czas_snu > 0:
                print(f"Czekam {round(czas_snu, 2)} sekund do kolejnego sprawdzenia...")
                time.sleep(czas_snu)
            else:
                print("Ostrzeżenie: Cykl trwał dłużej niż czas oczekiwania. Sprawdzam ponownie natychmiast.")


    except KeyboardInterrupt:
        print("\nMonitorowanie przerwane przez użytkownika (Ctrl+C).")



if __name__ == "__main__":
    monitor_olx()