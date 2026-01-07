# SOSPOS Garson V2

SOSPOS Garson, restoran garsonları için geliştirilmiş bir sipariş yönetim uygulamasıdır. Android (Kotlin) ve Flask backend ile çalışır.

## 📱 Özellikler

### Android Uygulaması
- ✅ Kullanıcı girişi
- ✅ Masa görüntüleme ve yönetimi
- ✅ Menü listesi ve kategorilere göre filtreleme
- ✅ Sipariş ekleme ve yönetimi
- ✅ Siparişleri mutfağa gönderme
- ✅ Masa kapatma
- ✅ Anlık fiyat hesaplama

### Backend API
- ✅ RESTful API yapısı
- ✅ Kullanıcı authentication
- ✅ Masa yönetimi
- ✅ Menü yönetimi
- ✅ Sipariş yönetimi
- ✅ CORS desteği

## 🛠️ Teknolojiler

### Android
- **Dil**: Kotlin
- **Build System**: Gradle (Kotlin DSL)
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34
- **Kütüphaneler**:
  - AndroidX (Core, AppCompat, Material Design)
  - Retrofit 2.9.0 (API iletişimi)
  - Gson (JSON parsing)
  - Coroutines (Asenkron işlemler)
  - RecyclerView & CardView

### Backend
- **Framework**: Flask 3.0.0
- **Python**: 3.8+
- **Kütüphaneler**:
  - flask-cors 4.0.0
  - Werkzeug 3.0.1

## 📦 Kurulum

### Backend Kurulumu

1. Python 3.8+ kurulu olduğundan emin olun:
```bash
python --version
```

2. Backend dizinine gidin:
```bash
cd backend
```

3. Gerekli paketleri kurun:
```bash
pip install -r requirements.txt
```

4. Flask uygulamasını başlatın:
```bash
python app.py
```

Backend `http://localhost:5000` adresinde çalışmaya başlayacaktır.

### Android Kurulumu

1. Android Studio'yu açın (Arctic Fox veya daha yeni sürüm)

2. `android` klasörünü Android Studio'da açın

3. Gradle sync işleminin tamamlanmasını bekleyin

4. API bağlantısını yapılandırın:
   - `android/app/src/main/java/com/sospos/garson/api/RetrofitClient.kt` dosyasını açın
   - `BASE_URL` değişkenini backend sunucu adresinize göre değiştirin:
     - Emulator için: `http://10.0.2.2:5000/`
     - Gerçek cihaz için: `http://BACKEND_IP:5000/`

5. Uygulamayı çalıştırın (Run > Run 'app')

## 📱 Kullanım

### Giriş Bilgileri

Backend'de tanımlı test kullanıcıları:

- **Garson**
  - Kullanıcı adı: `garson1`
  - Şifre: `123456`

- **Admin**
  - Kullanıcı adı: `admin`
  - Şifre: `admin123`

### Uygulama Akışı

1. **Giriş**: Kullanıcı adı ve şifre ile giriş yapın
2. **Masalar**: Ana ekranda tüm masaları görüntüleyin
   - Yeşil: Boş masa
   - Kırmızı: Dolu masa
3. **Masa Seçimi**: Bir masaya tıklayın
4. **Sipariş Ekleme**: "Sipariş Ekle" butonu ile menüyü açın
5. **Menü**: Kategorilere göre ürünleri görüntüleyin ve sipariş ekleyin
6. **Sipariş Yönetimi**: Siparişleri görüntüleyin ve mutfağa gönderin
7. **Masa Kapatma**: İşlem tamamlandığında masayı kapatın

## 🗂️ Proje Yapısı

```
SOSPOSGarsonV2/
├── android/                          # Android projesi
│   ├── app/
│   │   ├── src/
│   │   │   └── main/
│   │   │       ├── java/com/sospos/garson/
│   │   │       │   ├── adapter/      # RecyclerView adapter'ları
│   │   │       │   │   ├── MenuAdapter.kt
│   │   │       │   │   ├── OrderAdapter.kt
│   │   │       │   │   └── TableAdapter.kt
│   │   │       │   ├── api/          # API servisleri
│   │   │       │   │   ├── ApiService.kt
│   │   │       │   │   └── RetrofitClient.kt
│   │   │       │   ├── model/        # Data modelleri
│   │   │       │   │   ├── ApiResponse.kt
│   │   │       │   │   ├── MenuItem.kt
│   │   │       │   │   ├── Order.kt
│   │   │       │   │   ├── Table.kt
│   │   │       │   │   └── User.kt
│   │   │       │   ├── LoginActivity.kt
│   │   │       │   ├── MainActivity.kt
│   │   │       │   ├── MenuActivity.kt
│   │   │       │   └── TableActivity.kt
│   │   │       ├── res/
│   │   │       │   ├── layout/       # XML layout'lar
│   │   │       │   ├── values/       # Strings, colors, themes
│   │   │       │   └── drawable/     # İkonlar ve drawable'lar
│   │   │       └── AndroidManifest.xml
│   │   └── build.gradle.kts          # App modül build dosyası
│   ├── build.gradle.kts              # Root build dosyası
│   ├── settings.gradle.kts
│   └── gradle.properties
│
├── backend/                          # Flask backend
│   ├── app.py                        # Ana Flask uygulaması
│   └── requirements.txt              # Python bağımlılıkları
│
└── README.md                         # Bu dosya
```

## 🔌 API Endpoints

### Authentication
- `POST /api/login` - Kullanıcı girişi

### Tables
- `GET /api/tables` - Tüm masaları listele
- `GET /api/tables/{id}` - Belirli bir masayı getir
- `PUT /api/tables/{id}` - Masa durumunu güncelle

### Menu
- `GET /api/menu` - Tüm menü öğelerini listele
- `GET /api/menu/category/{category}` - Kategoriye göre menü öğeleri

### Orders
- `GET /api/orders/table/{tableId}` - Masaya ait siparişleri getir
- `POST /api/orders` - Yeni sipariş oluştur
- `PUT /api/orders/{id}` - Sipariş güncelle
- `POST /api/orders/send-to-kitchen` - Siparişleri mutfağa gönder
- `DELETE /api/orders/{id}` - Sipariş sil

## 📝 Notlar

- Backend şu an in-memory database kullanmaktadır. Production ortamında PostgreSQL, MySQL veya MongoDB gibi gerçek bir veritabanı kullanılmalıdır.
- Güvenlik için JWT token authentication'ı tam olarak implement edilmelidir.
- Production ortamında HTTPS kullanılmalıdır.
- Emulator'de test edilirken `10.0.2.2` IP adresi localhost'u temsil eder.

## 🚀 Geliştirme

### Yeni Özellik Ekleme

1. Backend'e yeni endpoint ekleyin (app.py)
2. Android'de yeni API methodunu tanımlayın (ApiService.kt)
3. Gerekli UI değişikliklerini yapın
4. Test edin

### Veritabanı Değişiklikleri

Backend'de `users`, `tables`, `menu_items`, ve `orders` listelerini düzenleyebilirsiniz. Production ortamında bunlar gerçek bir veritabanına taşınmalıdır.

## 📄 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

## 👤 Geliştirici

SOSPOS Garson V2 - 2026
