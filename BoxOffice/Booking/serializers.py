from rest_framework import serializers
from taggit_serializer.serializers import TaggitSerializer, TagListSerializerField

from users.serializers import UserSerializer
from .models import *



class MovieSerializer(serializers.ModelSerializer):

    class Meta:

        model = Movie
        fields = '__all__'


class CinemaSerializer(serializers.ModelSerializer):

    class Meta:

        model = Cinema
        fields = '__all__'






class ShowingSerializer(serializers.ModelSerializer):

    class Meta:

        model = Showing
        fields = '__all__'


class SeatSerializer(serializers.ModelSerializer):

    class Meta:

        model = Seat
        fields = '__all__'

class CitySerializer(serializers.ModelSerializer):

    class Meta:

        model = City
        fields = '__all__'
class BookSerializer(serializers.ModelSerializer):

    class Meta:

        model = Book
        depth=1
        fields = '__all__'