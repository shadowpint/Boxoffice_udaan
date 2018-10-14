from django.contrib.auth.models import User
from django.db import models

# Create your models here.
from django.db.models import CharField
from django.db.models.expressions import RawSQL
from django_mysql.models import ListCharField
from taggit.managers import TaggableManager
class LocationManager(models.Manager):
    def nearby(self, lat, lng, proximity):
        """
        Return all object which distance to specified coordinates
        is less than proximity given in kilometers
        """
        # Great circle distance formula
        gcd = """
              6371 * acos(
               cos(radians(%s)) * cos(radians(lat))
               * cos(radians(lng) - radians(%s)) +
               sin(radians(%s)) * sin(radians(lat))
              )
              """
        return self.get_queryset()\
                   .exclude(lat=None)\
                   .exclude(lng=None)\
                   .annotate(distance=RawSQL(gcd, (lat,
                                                   lng,
                                                   lat)))\
                   .filter(distance__lt=proximity)\
                   .order_by('distance')
class City(models.Model):
    name = models.CharField(max_length=1000)
    lat = models.DecimalField(max_digits=8, decimal_places=4)
    lng = models.DecimalField(max_digits=8, decimal_places=4)
    leadImageUrl = models.CharField(max_length=1000, blank=True)

    def __str__(self):
        return self.name





class Cinema(models.Model):
    objects = LocationManager()
    cinema_name = models.CharField(max_length=200)
    cinema_addr = models.CharField(max_length=200)
    cinema_contact = models.CharField(max_length=20)
    cinema_city = models.ForeignKey(City)
    lat = models.DecimalField(max_digits=8, decimal_places=4)
    lng = models.DecimalField(max_digits=8, decimal_places=4)
    leadImageUrl = models.CharField(max_length=1000, blank=True)
    def __str__(self):
        return str(self.cinema_name) + "-" + str(self.cinema_city.name)
class Movie(models.Model):
    movie_title = models.CharField(max_length=200)
    poster = models.CharField(max_length=200)
    description = models.CharField(max_length=200)
    genre = models.CharField(max_length=200)
    cinema= models.ForeignKey(Cinema)
    release_date = models.DateTimeField('date released')


    def __str__(self):
        return self.movie_title






class Showing(models.Model):
    showing_time = models.DateTimeField('show time')
    showing_date = models.DateTimeField('show date')
    seat = ListCharField(
        base_field=CharField(max_length=3),
        size=80,
        max_length=(80 * 4))
    seat_booked = ListCharField(
        base_field=CharField(max_length=3,blank=True),
        size=80,
        max_length=(80 * 4))
    cinema = models.ForeignKey(Cinema, on_delete=models.CASCADE, default=1)
    movie = models.ForeignKey(Movie, on_delete=models.CASCADE, default=1)
    def __str__(self):
        return str(self.cinema.cinema_name)+"-"+str(self.movie.movie_title)

class Seat(models.Model):
    seat = models.CharField(primary_key=True, max_length=200)

    def __str__(self):
        return self.seat
class Book(models.Model):
    user = models.ForeignKey(User)
    show = models.ForeignKey(Showing)
    seat_booked = ListCharField(
        base_field=CharField(max_length=3, blank=True),
        size=80,
        max_length=(80 * 4))
    def __str__(self):
        return self.user.username