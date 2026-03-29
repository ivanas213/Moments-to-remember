"use client"

import { EventAPI } from "@/services/api"
import { Event } from "@/types/Event"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation";
import { User } from "@/types/User";
export default function Main(){
    const [events, setEvents] = useState<Event[]>([])
    const [loading, setLoading] = useState(true)
    const router = useRouter()
    const userJson = localStorage.getItem("user")
    const user: User = userJson != null ? JSON.parse(userJson): null
    if(!user || user.role != "buyer") router.push("/")

    useEffect(()=>{
        const fetchEvents = async ()=>{
            try{
                const res = await EventAPI.getEvents()
                setEvents(res.data)
            }
            catch (err) {
                console.error("Greška:", err);
            } finally {
                setLoading(false);
            }
        }
        fetchEvents()
    }, [])
    return(<div className="min-h-screen bg-brand ">
        <div className="h-12 bg-pink-header flex items-center">
                <img
                    src="images/logo.png"
                    className="mt-2 h-14 w-14 ml-2"
                />
                <div className="mx-1" />
                <h1 className = "text-black font-italianno font-base text-gray-950 text-2xl">Trenuci za pamcenje</h1>
                <div className="mx-48" />
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border" onClick={() => router.push("/offers")}>PONUDE</div>
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border" onClick={() => router.push("/notifications")}>OBAVEŠTENJA</div>
                <div className="bg-pink-selected px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-selected" onClick={() => router.push("/offers")}>DOGAĐAJI</div>
                <div className="mx-6" />
                <img
                    src="images/korpa.png"
                    className="mt-2 h-8 w-8 ml-2"
                    onClick={() => router.push("/cart")}
                />
                <div className="mx-1" />
                <img
                    src="images/profil.png"
                    className="mt-2 h-8 w-8 ml-2"
                    onClick={() => router.push("/profile")}
                />
            </div>
            <div className="h-9" />            
            <h1 className="text-center text-gray-900 font-lunasima text-5xl">ORGANIZOVANI DOGAĐAJI</h1>
            <div className="h-9" />            
              <div className="flex flex-col gap-6 p-6">
            {events.map((event) => (
              <div
                key={event.title}
                className="flex px-6"
              >
                <div className="md:w-3/5 px-3.5">
                  <img
                    src={event.imageUrl}
                    alt={event.title}
                    className="w-full h-80 object-cover"
                  />
                </div>

                <div className="md:w-2/5 bg-pink-event p-6 flex flex-col justify-center">
                  <h2 className="text-2xl text-black font-bold mb-2 text-center">{event.title}</h2>
                  <h2 className="text-2xl text-black font-bold mb-2 text-center">({event.date})</h2>
                  <p className="text-black">{event.description}</p>
                </div>
              </div>
            ))}</div>
    </div>)
}