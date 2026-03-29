"use client";

import { EventAPI, PromotionAPI } from "@/services/api"
import { Promotion } from "@/types/Promotion"
import { Event } from "@/types/Event";
import Image from "next/image"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation";
import { User } from "@/types/User";
const getPromotions = async ()=>{
    try{
      await PromotionAPI.getPromotions()
    }
    catch(err){ 
      console.log(err)
    }
  }
export default function Main(){
    const [promotions, setPromotions] = useState<Promotion[]>([]);
    const [events, setEvents] = useState<Event[]>([]);
    const [currentIndex, setCurrentIndex] = useState(0)
    const [loading, setLoading] = useState(true);
    const userJson = localStorage.getItem("user")
    const user: User = userJson != null ? JSON.parse(userJson): null
    const router = useRouter()
    if(!user || user.role != "buyer") router.push("/")

      useEffect(() => {
        const fetchPromotions = async () => {
          try {
            const res = await PromotionAPI.getPromotions();
            setPromotions(res.data.filter((promotion: Promotion) => promotion.web === true));
          } catch (err) {
            console.error("Greška:", err);
          } finally {
            setLoading(false);
          }
        };
        fetchPromotions();
    }, []); 
    useEffect(() => {
        const fetchEvents = async () => {
          try {
            const res = await EventAPI.getEvents();
            setEvents(res.data);
          } catch (err) {
            console.error("Greška:", err);
          } finally {
            setLoading(false);
          }
        };
        fetchEvents();
    }, []); 
    useEffect(() => {
      if (promotions.length === 0) return;
      const interval = setInterval(() => {
        setCurrentIndex((prev) =>
          prev === promotions.length - 1 ? 0 : prev + 1
        );
      }, 20000); 
      return () => clearInterval(interval); 
    }, [promotions]);
    return (
        <div className="min-h-screen bg-brand ">
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
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border" onClick={() => router.push("/events")}>DOGAĐAJI</div>
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
            <div className="relative w-full" style={{ aspectRatio: "1440 / 641" }}>
              {!loading && promotions.length > 0 && (
                <>
                   {!loading && promotions.length > 0 && (
                  <Image
                    src={promotions[currentIndex].imageUrl}
                    alt={promotions[currentIndex].description ?? "Promotion"}
                    fill
                    className="object-cover rounded"
                  />
                )}
                  <button
                    onClick={() =>
                      setCurrentIndex((prev) =>
                        prev === 0 ? promotions.length - 1 : prev - 1
                      )
                    }
                    className="absolute top-1/2 left-4 -translate-y-1/2 bg-black/40 text-white px-3 py-2 rounded-full hover:bg-black/60"
                  >
                    ‹
                  </button>

                  <button
                    onClick={() =>
                      setCurrentIndex((prev) =>
                        prev === promotions.length - 1 ? 0 : prev + 1
                      )
                    }
                    className="absolute top-1/2 right-4 -translate-y-1/2 bg-black/40 text-white px-3 py-2 rounded-full hover:bg-black/60"
                  >
                    ›
                  </button>
                </>
              )}
            </div>
            <div className="h-18" />            
            <h1 className="text-center text-gray-900 font-lunasima text-6xl">ORGANIZOVANI DOGAĐAJI</h1>
            <div className="h-18" />            
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
      ))}
      <div className="h18"></div>
      <h1 className="text-center text-pink-text-footer font-lunasima" onClick={() => {router.push("/events")}}>Pogledaj više događaja</h1>
      <hr className=" border-black" />
  <div className="mx-auto justify-around items-center text-black">
    
    <div className="flex items-center gap-3 py-2">
      <img src="/images/footer_telefon.png" alt="Telefon" className="w-6 h-6" />
      <p className="text-sm md:text-base">
        <span className="font-semibold">Telefon:</span> +381 64 123 45 67
      </p>
    </div>

    <div className="flex items-center gap-3 py-2">
      <img src="/images/footer_email.png" alt="Email" className="w-6 h-6" />
      <p className="text-sm md:text-base">
        <span className="font-semibold">Email adresa:</span> trenutcizapamcenje@gmail.com
      </p>
    </div>

    <div className="flex items-center gap-3 py-2">
      <img src="/images/footer_radno_vreme.png" alt="Radno vreme" className="w-6 h-6" />
      <p className="text-sm md:text-base">
        <span className="font-semibold">Radno vreme:</span> Ponedeljak–Petak, 8–20 h
      </p>
    </div>

  </div>
</div>

    </div>

    )
}