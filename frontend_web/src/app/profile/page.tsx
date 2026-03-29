"use client"

import { OfferAPI } from "@/services/api"
import { Offer } from "@/types/Offer"
import { User } from "@/types/User"
import { useRouter } from "next/navigation"
import { useEffect, useState } from "react"

export default function Main(){
    const [offers, setOffers] = useState<Offer[]>([])
    const [loading, setLoading] = useState(true)
    const router = useRouter()
    const userJson = localStorage.getItem("user")
    const user: User = userJson!= null ? JSON.parse(userJson): null
    if(!user) router.push("/")


    useEffect(()=>{
        const fetchOffers = async ()=>{
            try{
                const res = await OfferAPI.getOffers()
                setOffers(res.data)
            }
            catch (err) {
                console.error("Greška:", err);
            } finally {
                setLoading(false);
            }
        }
        fetchOffers()
    }, [])
    return(<div className="min-h-screen bg-brand ">
       {user.role =="buyer" && (<div className="h-12 bg-pink-header flex items-center">
                <img
                    src="../images/logo.png"
                    className="mt-2 h-14 w-14 ml-2"
                    onClick={() => router.push("/main")}
                />
                <div className="mx-1" />
                <h1 className = "text-black font-italianno font-base text-gray-950 text-2xl"  onClick={() => router.push("/main")}>Trenuci za pamcenje</h1>
                <div className="mx-48" />
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border" onClick={() => router.push("/offers")}>PONUDE</div>
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border"  onClick={() => router.push("/notifications")}>OBAVEŠTENJA</div>
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border"  onClick={() => router.push("/events")}>DOGAĐAJI</div>
                <div className="mx-6" />
                <img
                    src="../images/korpa.png"
                    className="mt-2 h-8 w-8 ml-2"
                     onClick={() => router.push("/cart")}
                />
                <div className="mx-1" />
                <img
                    src="../images/profil_izabran.png"
                    className="mt-2 h-8 w-8 ml-2"
                  
                />
            </div>)}
            {user.role =="organiser" && (<header className="h-12 bg-pink-header flex items-center justify-between px-3">
            <div className="flex items-center gap-2">
            <img
                src="/images/logo.png"
                className="h-8 w-8"
                alt="Logo"
            />
            <h1 className="text-gray-950 font-italianno text-xl">
                Trenuci za pamćenje
            </h1>
            </div>

            <div className="flex items-center gap-2" onClick={() => router.push("/profile")}>
            <span className="text-black">Profil</span>
            <img
                src="/images/profil.png"
                className="h-6 w-6"
                alt="Profil"
            />
            </div>
        </header>)}
            <div className="min-h-screen flex items-center justify-center w-full">
    <div className="bg-white rounded-2xl shadow-lg p-6 w-[25rem] relative mx-auto">
    <button className="absolute top-8 right-8">
      <img src="/images/odjava.png" alt="Logout" className="w-6 h-6" onClick={() => {localStorage.removeItem("user"); router.push("/")}} />
    </button>

    <div className="flex flex-col items-center">
      <img src="/images/profil_covek.png" alt="User" className="w-12 h-12" />
      <h2 className="text-xl font-bold text-black mt-2">Moj profil</h2>
    </div>

    <hr className="border-black my-4" />

    <div className="mt-6 mx-auto w-full max-w-md grid grid-cols-2 gap-x-6 gap-y-3">
        <div className="text-right font-medium text-black">Ime:</div>
        <div className="text-left text-black">{user.first_name}</div>

        <div className="text-right font-medium text-black">Prezime:</div>
        <div className="text-left text-black">{user.last_name}</div>

        <div className="text-right font-medium text-black">Adresa:</div>
        <div className="text-left text-black">{user.address}</div>

        <div className="text-right font-medium text-black">Kontakt telefon:</div>
        <div className="text-left text-black">{user.phone_number}</div>
        <button className="text-black bg-pink-light text-right ml-8 pr-2 rounded-2xl" onClick={() => router.push("/profile/change_data")}>Izmena podataka</button>
        <button className="text-black bg-pink-light text-left mr-8 pl-2 rounded-2xl" onClick={() => router.push("/profile/change_password")}>Izmena lozinke</button>
    </div>
  </div>
</div>

    </div>)
}