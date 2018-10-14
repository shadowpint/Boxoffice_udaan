package com.udaan.boxofficeapp;

import android.net.Uri;

public class Config {

    public static final String ENDPOINT_URL = "https://boxofficeapi.herokuapp.com";
    //public static final String ENDPOINT_URL = "http://192.168.0.100/minris/web/app_dev.php";
    public static final String BASE_URL_IMAGE = "https://boxofficeapi.herokuapp.com/bundles/assets/basics";
    //public static final String BASE_URL_IMAGE = "http://192.168.0.100/minris/web/bundles/assets/basics";

    public static final String PATH_IMAGE_PRODUCT = "/products";
    public static final String PATH_IMAGE_BLOG_ENTRY = "/reports";
    public static final String PATH_IMAGE_SUBCATEGORIES = "/subcategories";

    public static final String OAUTH_CLIENT_ID = "JCuudKNlI8uBMzXXuqrOlbEGXqnQwQWADNepAaZx";
    public static final String OAUTH_CLIENT_SECRET = "WP2aGFDQid456EwnCodYmehCvRyU3vzBlhWgHhaCKVSJLHs6Vx9NkpY3a5H3TxqmqKleL5V5oTf5fxuMF6WFkOhcIGGk7TnWPfu5dl1mzIO0CH8t129vQGRBh6Q9QYU8";
    public static final String OAUTH_GRANT_TYPE_PASSWORD = "password";
    public static final String OAUTH_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    public static String buildProductImageUrl(String imageFilename) {
        return buildImageUrlWithPath(PATH_IMAGE_PRODUCT, imageFilename);
    }

    public static String buildSubcategoryImageUrl(String imageFilename) {
        return buildImageUrlWithPath(PATH_IMAGE_SUBCATEGORIES, imageFilename);
    }

    public static String buildBlogEntryImageUrl(String imageFilename) {
        return buildImageUrlWithPath(PATH_IMAGE_BLOG_ENTRY, imageFilename);
    }

    private static String buildImageUrlWithPath(String path, String imageFilename) {
        if (imageFilename == null) {
            return null;
        }
        return new StringBuilder()
                .append(BASE_URL_IMAGE)
                .append(path)
                .append('/')
                .append(imageFilename)
                .toString();
    }

    public static Uri buildProductImageUrlByCodeAndColor(String productCode, long colorId) {
        return Uri.parse(ENDPOINT_URL + "/web-service/shoppingCart/product/image")
                .buildUpon()
                .appendQueryParameter("productCode", productCode)
                .appendQueryParameter("colorId", Long.toString(colorId))
                .build();
    }

}
