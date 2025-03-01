import psycopg2
import requests
import sys

DB_URL=""
HUGGINGFACE_API_KEY=""

API_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-mnli"
HEADERS = {"Authorization": f"Bearer {HUGGINGFACE_API_KEY}"}

def fetch_categories():
    conn = psycopg2.connect(DB_URL)
    cur = conn.cursor()

    cur.execute("SELECT id, name FROM categories")
    categories = {row[1]: row[0] for row in cur.fetchall()}

    cur.close()
    conn.close()
    return categories

def classify_poem(title, content, categories):
    text = f"{title}. {content}"
    payload = {"inputs": text, "parameters": {"candidate_labels": list(categories.keys())}}

    response = requests.post(API_URL, headers=HEADERS, json=payload)

    if response.status_code == 200:
        result = response.json()
        best_category_name = result["labels"][0]
        return best_category_name
    else:
        print(f"Error: {response.text}")
        return None

def main():
    if len(sys.argv) != 3:
        print("Usage: python classify_poem.py <title> <content>")
        return

    title = sys.argv[1]
    content = sys.argv[2]

    categories = fetch_categories()

    best_category = classify_poem(title, content, categories)

    if best_category:
        print(best_category)
    else:
        print("Failed to classify the poem.")

if __name__ == "__main__":
    main()
