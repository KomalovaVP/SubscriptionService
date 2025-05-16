package com.example.subscriptionservice.model;

public enum SubscriptionName {
    YOUTUBE_PREMIUM("YouTube Premium"),
    VK_MUSIC("VK Музыка"),
    YANDEX_MUSIC("Яндекс.Музыка"),
    NETFLIX("Netflix"),
    SPOTIFY("Spotify"),
    IVI("IVI"),
    OKKO("Okko"),
    DISNEY_PLUS("Disney+"),
    AMAZON_PRIME_VIDEO("Amazon Prime Video"),
    HBO_MAX("HBO Max");

    private final String caption;

    SubscriptionName(String caption) {
        this.caption = caption;
    }
}
