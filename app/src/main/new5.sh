# 1. Pastikan semua file ter-commit di branch lokal (misal: master)
git add .
git commit -m "Prepare for subtree push"

# 2. Gunakan filter-repo atau pindahkan file ke subfolder secara lokal sebelum push
# Namun, cara termudah bagi kebanyakan pengguna adalah:
mkdir dice-roller
# Pindahkan semua file/folder (app, gradle, build.gradle.kts, dll) ke dalam dice-roller/
# Gunakan perintah 'mv' atau drag-drop di file explorer Anda
# Setelah folder terstruktur:
git add .
git commit -m "Move project to dice-roller folder"
git push origin master
