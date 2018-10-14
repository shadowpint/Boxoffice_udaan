from django.conf.urls import url
from django.conf import settings
from django.conf.urls.static import static

from Booking.views import *
from . import views


urlpatterns = [
    # Regular Django Views


    # API views
# url(r'^event_nearby/$', NearbyEvent.as_view()),
url(r'^movie/(?P<pk>[0-9]+)/$', MovieList.as_view()),
url(r'^cinema/(?P<pk>[0-9]+)/$', CinemaList.as_view()),
# url(r'^theatre/$', TheatreList.as_view()),
url(r'^showing/', views.ShowingList.as_view()),
url(r'^seat_available/', views.SeatAvailable.as_view()),
url(r'^seat/', views.Seat.as_view()),
url(r'^book/', views.Booking.as_view()),
url(r'^cinema_nearby/', views.NearbyCinema.as_view()),
# url(r'^price/(?P<pk>[0-9]+)/$', views.PriceView.as_view()),
# url(r'^productdetail/(?P<pk>[0-9]+)/$', views.ProductDetail.as_view()),
url(r'^city/$',CityList.as_view()),
url(r'^seat/$',SeatList.as_view()),
# url(r'^shoppingCart/(?P<pk>[0-9]+)/$', views.CartDetail.as_view()),
# url(r'^address-book/$', AddressList.as_view()),
# url(r'^orders/$', OrderList.as_view()),
# url(r'^categories/$', CategoryList.as_view()),
# url(r'^subcategories/$', SubcategoryList.as_view()),
# url(r'^brands/$', BrandList.as_view()),
] + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
