# Mijoz

Mijozlar uchun taksi xizmatidan foydalanish uchun qulay dastur

## Topshiriqlar (TODO)
1. Unused files:
    - btn_red_gradient
    - dialog_menu

2. Widget yaratish kerak:
    - U orqali buyurtma berish
    - Buyurtmalar soni

3. Resources
    - https://taxi.yandex.uz/uz_uz/
    - https://www.vecteezy.com/vector-art/4725829-online-taxi-booking-travel-service-flat-design-illustration-via-mobile-app-on-smartphone-take-someone-to-a-destination-suitable-for-background-poster-or-banner
    - https://www.svgrepo.com/collection/iconwrap-filled-duotone-interface-icons/
    - https://www.iconfinder.com/icons/5208408/arrow_forward_navigation_send_share_icon
    - https://ru.freepik.com/premium-vector/types-of-men-activities-and-recreation_9352715.htm#from_view=detail_author
    - https://dribbble.com/signup/new

1. 4.16 mb
2. 5.11 mb: After add loader library

## References

* [Java](https://www.oracle.com/cis/java/) Java is a high-level, class-based, object-oriented programming language that is
  designed to have as few implementation dependencies as possible
* [Android](https://developer.android.com/topic/architecture/intro) Whether you're building for the
  phone, the wrist, tablets, TVs, or cars, we have the guides and API reference you need.
* [JitPack](https://jitpack.io/) JitPack is a novel package repository for JVM and Android projects.
  Easy to use package repository for Git.
* [Metarial UI](https://developer.android.com/develop/ui/views/theming/look-and-feel) Material
  Design is a comprehensive guide for visual, motion, and interaction design across platforms and
  devices.
* [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
* [Picasso](https://square.github.io/picasso/) A powerful image downloading and caching library for
  Android.
* [EventBus](https://greenrobot.org/eventbus/) EventBus is an open-source library for Android and Java using the
  publisher/subscriber pattern for loose coupling.
* [SocketIO](https://github.com/socketio/socket.io-client-java) This is the Socket.IO Client Library for Java, which is
  simply ported from the JavaScript client.
* [Android-SpinKit](https://ybq.github.io/Android-SpinKit/) Android loading animations
* [Pulsator4Droid](https://github.com/booncol/Pulsator4Droid) Pulse animation for Android
* [OSM Map](https://osmdroid.github.io/osmdroid/) Osmdroid is a (almost) full/free replacement for Android's MapView

## Features
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding)


### Location
- LocationManager.GPS_PROVIDER - gps orqali joylashuvni aniqlaydi
- LocationManager.NETWORK_PROVIDER - Bu tarmoq orqali joylashuvni aniqlaydi.
- LocationManager.NETWORK_PROVIDER need network, but it is real, not precise. if you want to use
  LocationManager.GPS_PROVIDER, the situation must be outdoor instead of indoor, because GPS location need satellite, if you
  are in any building, the satellite cannot find you! pleasle go outdoor and check with GPS_PROVIDER again!

* best approach is getting location from both of GPS and NETWORK and check any of then that was not null and more accurate
  using it.


### Description
- ic -> icon
- im -> image
