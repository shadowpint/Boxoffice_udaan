from django.contrib import admin

# Register your models here.
from Booking.models import *

admin.site.register(City)
admin.site.register(Movie)
admin.site.register(Cinema)

admin.site.register(Showing)
admin.site.register(Seat)
admin.site.register(Book)