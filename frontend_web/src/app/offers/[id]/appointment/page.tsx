"use client"

import { AppointmentAPI, OfferAPI } from "@/services/api"
import { Offer } from "@/types/Offer"
import { useParams, useRouter } from "next/navigation"
import { useEffect, useState } from "react"
import Image from "next/image"
import { Router } from "next/router"
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime"
import { AppointmentRequest } from "@/types/AppointmentRequest"
import { User } from "@/types/User"
export default function Main(){
    const [currentPage, setCurrentPage] = useState(1)
    const [guests, setGuests] = useState(0)
    const [date, setDate] = useState("10.10.2025")
    const { id } = useParams<{ id: string }>();
    const offersJson = localStorage.getItem("offers")
    const offers: Offer[] = offersJson != null ? JSON.parse(offersJson): null
    const myOffer = offers.find(o => o._id === id)
    const userJson = localStorage.getItem("user")
    const user: User = userJson != null ? JSON.parse(userJson): null
    const router = useRouter()
    if(user.role != "buyer") router.push("/")
    return(<div className="min-h-screen bg-brand ">
        
        <div className="h-12 bg-pink-header flex items-center">
                <img
                    src="../../images/logo.png"
                    className="mt-2 h-14 w-14 ml-2"
                    onClick={() => router.push("/main")}
                />
                <div className="mx-1" />
                <h1 className = "text-black font-italianno font-base text-gray-950 text-2xl"  onClick={() => router.push("/main")}>Trenuci za pamcenje</h1>
                <div className="mx-48" />
                <div className="bg-pink-selected px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border">PONUDE</div>
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border"  onClick={() => router.push("/notifications")}>OBAVEŠTENJA</div>
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border"  onClick={() => router.push("/events")}>DOGAĐAJI</div>
                <div className="mx-6" />
                <img
                    src="../../images/korpa.png"
                    className="mt-2 h-8 w-8 ml-2"
                    onClick={() => router.push("/cart")}
                />
                <div className="mx-1" />
                <img
                    src="../../images/profil.png"
                    className="mt-2 h-8 w-8 ml-2"
                    onClick={() => router.push("/profile")}
                />
            </div>
            <div className="h-9" />     
            <div className="flex flex-col items-center min-w-screen">
                <h1 className="text-center text-gray-900 font-lunasima text-5xl ">
                    {myOffer?.name} (sala {myOffer?.hall.name})
                </h1>
                <h1 className="h-9"/>
            </div>
            <div className="bg-white rounded-2xl shadow-lg p-6 w-[25rem] relative mx-auto">
            <div className="flex items-center gap-3">
            <img src="../../images/tortica.png" alt="" className="h-12 w-auto" />
            <p className="text-black">
            Naš tim je posvećen tome da vaš dan protekne besprekorno. Popunite jednostavan
            formular i uskoro ćemo vas kontaktirati kako bismo dogovorili sve detalje.
            </p>
            </div>
                <hr className="border-gray-500 my-4" />
                <h1 className="h-2"/>
                <h1 className="text-black px-2">Cena po gostu: {myOffer?.price}€</h1>
                <hr className="border-gray-500 my-4" />
                    <div className="mt-6 mx-auto w-full max-w-md grid grid-cols-2 gap-x-6 gap-y-3">
                    <h1 className="text-black px-2 text-left font-medium">Broj gostiju: <br /> (min {myOffer?.minGuests}, max {myOffer?.maxGuests})</h1>
                    <input
                        id="guests"
                        type="number"
                        className="w-full border border-gray-300 rounded-lg px-3 py-2 text-black placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-pink-300 text-left"
                        placeholder={guests.toString()}
                        onChange={(e) => setGuests(Number(e.target.value))} 
                    />
                    <h1 className="text-black px-2 text-left font-medium">Datum:</h1>
                    <input
                        id="date"
                        type="string"
                        className="w-full border border-gray-300 rounded-lg px-3 py-2 text-black placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-pink-300 text-left"
                        placeholder={date.toString()}
                        onChange={(e) => setDate(e.target.value)} 
                    />
                    </div>
                    <hr className="border-gray-500 my-4" />
                    <h1 className="text-black">Ukupno: {(myOffer? myOffer.price: 0 )* guests} €</h1>
                    <div className="mt-6 mx-auto w-full max-w-md grid grid-cols-2 gap-x-6 gap-y-3">

                    <button className="text-black bg-pink-light text-left ml-16 pl-4 rounded-2xl" onClick={() => router.back()} >Otkaži</button>
                    <button className="text-black bg-pink-light text-left mr-16 pl-4 rounded-2xl disabled:opacity-50 disabled:cursor-not-allowed"   disabled={!myOffer || !guests || !date || guests > myOffer?.maxGuests || guests < myOffer?.minGuests}
 onClick={() => {AddToCart(router, "cart", date, myOffer? myOffer._id: "", user._id, guests) }} >Dodaj u korpu</button>
                    </div>
             </div>
             


    </div>)
}

function AddToCart(router: AppRouterInstance, status: string, date: string, offer: string, user: string, guests: number){
    const appointmentRequest: AppointmentRequest = {
        status, date, offer, user, guests
    }
    AppointmentAPI.addToCart(appointmentRequest)
    router.push("/main")
}