import psycopg2
import requests
import sys
import os

DB_URL = os.environ.get("DB_URL")
HUGGINGFACE_API_KEY = os.environ.get("HUGGINGFACE_API_KEY")

API_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-mnli"

HEADERS = {"Authorization": f"Bearer {HUGGINGFACE_API_KEY}"}

def fetch_categories_and_languages():
    conn = psycopg2.connect(DB_URL)
    cur = conn.cursor()

    cur.execute("SELECT id, name FROM categories")
    categories = {row[1]: row[0] for row in cur.fetchall()}

    cur.execute("SELECT id, name FROM languages")
    languages = {row[1]: row[0] for row in cur.fetchall()}

    cur.close()
    conn.close()
    return categories, languages

def classify_poem(text, labels):
    payload = {"inputs": text, "parameters": {"candidate_labels": list(labels.keys())}}

    response = requests.post(API_URL, headers=HEADERS, json=payload)

    if response.status_code == 200:
        result = response.json()
        best_label = result["labels"][0]
        return best_label
    else:
        print(f"Error: {response.text}")
        return None

def main():
    if len(sys.argv) != 3:
        print("Usage: python classify_poem.py <title> <content>")
        return

    title = sys.argv[1]
    content = sys.argv[2]
    text = f"{title}. {content}"

    categories, languages = fetch_categories_and_languages()

    best_category = classify_poem(text, categories)
    best_language = classify_poem(text, languages)

    if best_category and best_language:
        print(f"{best_category}|{best_language}")
    else:
        print("Failed to classify the poem.")

if __name__ == "__main__":
    main()
