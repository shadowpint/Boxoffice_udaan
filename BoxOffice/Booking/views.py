# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import itertools
import json

import django_filters
from django.http import Http404, HttpResponse
from django.shortcuts import render
from django.views.decorators.http import condition
from rest_framework import generics, filters, status
# Create your views here.
from rest_framework import generics
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.views import APIView

from Booking.models import *
from Booking.serializers import *
from rest_framework.response import Response
# Create your views here.
class CityFilter(filters.FilterSet):

    class Meta:
        model = City
        fields = '__all__'

class MovieFilter(filters.FilterSet):
    class Meta:
        model = Movie
        fields = '__all__'





class CinemaFilter(filters.FilterSet):
    class Meta:
        model = Cinema
        fields = '__all__'










class ShowingFilter(filters.FilterSet):

    class Meta:
        model = Showing
        fields = '__all__'
class BookFilter(filters.FilterSet):

    class Meta:
        model = Book
        fields = '__all__'

class SeatFilter(filters.FilterSet):

    class Meta:
        model = Seat
        fields = '__all__'










class MovieList(APIView):
    """
    Get / Create questions
    """
    queryset = Movie.objects.all()
    serializer_class = MovieSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = MovieFilter
    permission_classes = [AllowAny]

    def get_object(self, pk):
        try:
            return Movie.objects.filter(cinema_id=pk)
        except Movie.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = MovieSerializer(snippet,many=True)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = MovieSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    
    
class CinemaList(APIView):
    """
    Get / Create questions
    """
    queryset = Cinema.objects.all()
    serializer_class = CinemaSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = CinemaFilter
    permission_classes = [AllowAny]

    def get_object(self, pk):
        try:
            return Cinema.objects.filter(cinema_city_id=pk)
        except Cinema.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = CinemaSerializer(snippet,many=True)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        snippet = self.get_object(pk)
        serializer = CinemaSerializer(snippet, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, format=None):
        snippet = self.get_object(pk)
        snippet.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
    
    


class ShowingList(APIView):
    """
    Get / Create questions
    """
    queryset = Showing.objects.all()
    serializer_class = ShowingSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = ShowingFilter
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        data = request.GET
        print(data)
        showing = Showing.objects.filter(cinema_id=request.GET['cinema'],movie_id=request.GET['movie'])

        serializer = ShowingSerializer(showing,many=True)
        return Response(serializer.data)
class ShowingDetail(APIView):
    """
    Get / Update a Choice
    """

    queryset = Showing.objects.all()
    serializer_class = ShowingSerializer
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        data = request.data.copy()
        print(data)
        showing = Showing.objects.all

        serializer = ShowingSerializer(showing,many=True)
        return Response(serializer.data)


class CityList(APIView):
    """
    Get / Create questions
    """
    queryset = City.objects.all()
    serializer_class = CitySerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = CityFilter
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        data = request.data.copy()
        print(data)
        city = City.objects.all()

        serializer = CitySerializer(city,many=True)
        return Response(serializer.data)
class Seat(APIView):#Checking for seat availability on the basis of preferred choice seat
    """
    Get / Create questions
    """
    queryset =Book.objects.all()
    serializer_class = BookSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = BookFilter
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        data=request.GET.copy()
        print(data)
        inner_data = {}
        response_data = {}
        show = Showing.objects.get(movie_id=data['movie_id'],cinema_id=data['cinema_id']) #getting all the available seats
        print(show.seat)
        ls=show.seat
        if len(ls)>0:
            response_data['Seats'] = ls
            response_data['show_id'] = show.id
        else:
            response_data['Seats'] = "No seats Available"
        return Response(response_data, content_type="application/json")
class SeatAvailable(APIView):#Checking for seat availability on the basis of preferred choice seat
    """
    Get / Create questions
    """
    queryset =Book.objects.all()
    serializer_class = BookSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = BookFilter
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        data=request.GET.copy()
        print(data)
        inner_data = {}
        show = Showing.objects.get(movie_id=data['movie_id'],cinema_id=data['cinema_id']) #getting all the available seats
        print(show.seat)
        ls=show.seat
        seat_num=int(data['num']) #getting number of seats to be booked
        #print(seat_num)
        seat_choice=data['choice'] #choice of seat given by the user
        #print(seat_choice)
        num=int(seat_choice[1:])
        seat_char=seat_choice[:1] #getting row character from choice given
        available_list=[]
        for l in ls:
            if l[:1]==seat_char:
                available_list.append(l)    #creating own available seat list based on the choice given by the user
        print(available_list)


        ls2=[]
        ind=0
        response_data = {}

        if seat_choice in available_list: #checking if the seat choice is available in the list(the seat number)
            ind=available_list.index(seat_choice)
            #print("search_index= "+str(ind))
            if (ind-seat_num+1)>=0:
             begning_ind=ind-seat_num+1

             #print("begining_ind="+str(begning_ind))
            else:
                begning_ind=0

            for i in range(begning_ind,begning_ind+seat_num):
              if begning_ind+seat_num<=len(available_list):
               ls=[]
               for k in range(begning_ind,begning_ind+seat_num):       #checking for available combination of seats based on choice and number

                  if(k+1)<len(available_list):
                      if int(available_list[k][1:])+1!= int(available_list[k+1][1:]):
                          response_data['availableSeats'] = "No seats Available for your choice"

                      else:
                          ls.append(int(available_list[k][1:]))
               ls2.append(ls)
               begning_ind = begning_ind + 1
               print(ls2)

               for max in ls2:
                   if len(max)==seat_num:
                    inner_data[seat_char] = max
               response_data['availableSeats'] = inner_data
               response_data['show_id'] = show.id
               response_data['seat_row'] = seat_char
              print("inner_data")
              print(inner_data)
        else:
            response_data['availableSeats'] = "No seats Available for your choice"
        return Response(response_data, content_type="application/json")
def seatList(show,data):
    print(show.seat)
    ls = show.seat
    inner_data = {}
    seat_num = int(data['num'])  # getting number of seats to be booked
    # print(seat_num)
    seat_choice = data['choice']  # choice of seat given by the user
    # print(seat_choice)
    num = int(seat_choice[1:])
    seat_char = seat_choice[:1]  # getting row character from choice given
    available_list = []
    for l in ls:
        if l[:1] == seat_char:
            available_list.append(l)  # creating own available seat list based on the choice given by the user
    print(available_list)

    ls2 = []
    ind = 0
    response_data = {}

    if seat_choice in available_list:  # checking if the seat choice is available in the list(the seat number)
        ind = available_list.index(seat_choice)
        # print("search_index= "+str(ind))
        if (ind - seat_num + 1) >= 0:
            begning_ind = ind - seat_num + 1

            # print("begining_ind="+str(begning_ind))
        else:
            begning_ind = 0

        for i in range(begning_ind, begning_ind + seat_num):
            if begning_ind + seat_num <= len(available_list):
                ls = []
                for k in range(begning_ind,
                               begning_ind + seat_num):  # checking for available combination of seats based on choice and number

                    if (k + 1) < len(available_list):
                        if int(available_list[k][1:]) + 1 != int(available_list[k + 1][1:]):
                            response_data['availableSeats'] = "No seats Available for your choice"

                        else:
                            ls.append(int(available_list[k][1:]))
                ls2.append(ls)
                begning_ind = begning_ind + 1
                print(ls2)

                for max in ls2:
                    if len(max) == seat_num:
                        inner_data[seat_char] = max
    return inner_data
class Booking(APIView):
    """
    Get / Create questions
    """

    serializer_class = BookSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = BookFilter
    permission_classes = [IsAuthenticated]

    def post(self, request, format=None):
        data = request.data.copy()
        print(data)
        show = Showing.objects.get(id=data['show_id'])
        #print(show.seat)
        seat_row=data['choice'][:1]
        print("seart_row"+str(seat_row))
        seat_list=seatList(show,data)[seat_row]
        print("seat_list")
        print(seat_list)
        seat_actual_list=[]
        for i in seat_list:
            seat_actual_list.append(str(str(seat_row)+str(i)))
        print(seat_actual_list)
        complete_seat_list=show.seat



        for j in seat_actual_list:
            complete_seat_list.remove(j)

        print(complete_seat_list)
        show.seat=complete_seat_list
        seats_booked=show.seat_booked
        show.seat_booked=seats_booked+seat_actual_list
        show.save()

        book = Book.objects.create(user=request.user,seat_booked=seat_actual_list,show=Showing.objects.get(id=data['show_id']))
        book.save()
        serializer = BookSerializer(book)
        return Response(serializer.data)
# class CartList(APIView):
#     """
#     Get / Create questions
#     """
#     queryset = CartItem.objects.all()
#     serializer_class = CartSerializer
#     filter_backends = (filters.DjangoFilterBackend,)
#     filter_class = CartFilter
#     permission_classes = [IsAuthenticated]
#
#     def get(self, request, format=None):
#         id = request.user.pk
#         cart = CartItem.objects.filter(user=id)
#         serializer = CartSerializer(cart,many=True)
#         return Response(serializer.data)
#
#     def post(self, request, format=None):
#         data = request.data.copy()
#
#
#         cart = CartItem.objects.create(user=request.user,showingId=Showing.objects.get(id=data['showingId']),quantity=data['quantity'],leadImageUrl=Showing.objects.get(id=data['showingId']).leadImageUrl)
#
#         cart.save()
#         serializer = CartSerializer(cart)
#         return Response(serializer.data)

class NearbyCinema(APIView):
    """
    Get / Update a Choice
    """


    serializer_class = CinemaSerializer
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        data = request.data.copy()
        print(request.GET['lat'])

        cinema = Cinema.objects.nearby(request.GET['lat'],request.GET['lng'],150)

        serializer = CinemaSerializer(cinema,many=True)
        return Response(serializer.data)
class SeatList(APIView):
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        ls = ['A1', 'A2', 'A3', 'A4', 'A5', 'A6', 'A7', 'A8', 'A9', 'A10', 'B1', 'B2', 'B3', 'B4', 'B5', 'B6', 'B7',
              'B8', 'B9', 'B10', 'C1', 'C2', 'C3', 'C4', 'C5', 'C6', 'C7', 'C8', 'C9', 'C10', 'D1', 'D2', 'D3', 'D4',
              'D5', 'D6', 'D7', 'D8', 'D9', 'D10', 'E1', 'E2', 'E3', 'E4', 'E5', 'E6', 'E7', 'E8', 'E9', 'E10', 'F1',
              'F2', 'F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9', 'F10', 'G1', 'G2', 'G3', 'G4', 'G5', 'G6', 'G7', 'G8',
              'G9', 'G10', 'H1', 'H2', 'H3', 'H4', 'H5', 'H6', 'H7', 'H8', 'H9', 'H10']

        for i in ls:
            idb = Seat.objects.create(seat=i)


            idb.save()