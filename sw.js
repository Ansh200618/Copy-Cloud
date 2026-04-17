// Copy Cloud Service Worker – v5
// Provides offline shell caching and network-first strategy for pages.

const CACHE_NAME = 'copycloud-v5';

// Core shell assets to pre-cache on install
const PRECACHE_ASSETS = [
  '/',
  '/index.html',
  '/styles.css',
  '/favicon.svg?v=4',
  '/favicon.ico?v=4',
  '/favicon-96x96.png?v=4',
  '/apple-touch-icon.png?v=4',
  '/logo.png?v=4',
  '/web-app-manifest-192x192.png?v=4',
  '/web-app-manifest-512x512.png?v=4',
  '/site.webmanifest?v=4',
  '/privacy.html',
  '/terms.html'
];

// Install – cache the app shell
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(PRECACHE_ASSETS))
  );
  self.skipWaiting();
});

// Activate – clean up old caches
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((k) => k !== CACHE_NAME).map((k) => caches.delete(k)))
    )
  );
  self.clients.claim();
});

// Fetch – network-first for navigations, cache-first for static assets
self.addEventListener('fetch', (event) => {
  const { request } = event;

  // Skip non-GET and cross-origin requests
  if (request.method !== 'GET') return;
  if (!request.url.startsWith(self.location.origin)) return;

  // Navigation requests: network-first with cache fallback
  if (request.mode === 'navigate') {
    event.respondWith(
      fetch(request)
        .then((response) => {
          const clone = response.clone();
          caches.open(CACHE_NAME).then((cache) => cache.put(request, clone));
          return response;
        })
        .catch(() => caches.match(request).then((cached) => cached || caches.match('/index.html')))
    );
    return;
  }

  // API requests: network-first, no cache write for sensitive payloads
  if (request.url.includes('/api/')) {
    event.respondWith(
      fetch(request).catch(() => new Response(JSON.stringify({ error: 'Offline' }), {
        status: 503,
        headers: { 'content-type': 'application/json; charset=utf-8' }
      }))
    );
    return;
  }

  // Static assets: cache-first with network fallback
  if (request.destination === 'image' || request.url.match(/\.(png|jpg|jpeg|svg|ico|webp|woff2?)$/)) {
    event.respondWith(
      caches.match(request).then((cached) => {
        if (cached) return cached;
        return fetch(request).then((response) => {
          const clone = response.clone();
          caches.open(CACHE_NAME).then((cache) => cache.put(request, clone));
          return response;
        });
      })
    );
    return;
  }
});

// Web Push Notification support
self.addEventListener('push', (event) => {
  let payload = {};
  try { payload = event.data ? event.data.json() : {}; } catch {}
  const title = payload.title || 'Copy Cloud';
  const options = {
    body: payload.body || 'You received a new clip.',
    icon: '/web-app-manifest-192x192.png?v=4',
    badge: '/favicon-96x96.png?v=4',
    data: payload.url || '/retrieve'
  };
  event.waitUntil(self.registration.showNotification(title, options));
});

self.addEventListener('notificationclick', (event) => {
  const targetUrl = (event.notification && event.notification.data) || '/retrieve';
  event.notification.close();
  event.waitUntil(clients.openWindow(targetUrl));
});
