"use client"

import { AppointmentAPI, OfferAPI } from "@/services/api"
import { Appointment } from "@/types/Appointment"
import { Offer } from "@/types/Offer"
import { User } from "@/types/User"
import { useRouter } from "next/navigation"
import { useEffect, useState } from "react"

export default function Main(){
    const [offers, setOffers] = useState<Offer[]>([])
    const [loading, setLoading] = useState(true)
    const [notifications, setNotifications] = useState<Appointment[]>([])
    const userJson = localStorage.getItem("user")
    const user: User = userJson != null ? JSON.parse(userJson): null
    const router = useRouter()
    if(!user || user.role != "buyer") router.push("/")

    useEffect(() => {
        const fetchNotifications = async () => {
            try {
                const res = await AppointmentAPI.notifications(user._id);
                setNotifications(res.data);
                console.log(res.data)
            } catch (err) {
                console.error("Greška:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchNotifications();
    }, []); 
    return(<div className="min-h-screen bg-brand ">
        <div className="h-12 bg-pink-header flex items-center">
                <img
                    src="images/logo.png"
                    className="mt-2 h-14 w-14 ml-2"
                    onClick={() => router.push("/main")}
                />
                <div className="mx-1" />
                <h1 className = "text-black font-italianno font-base text-gray-950 text-2xl"  onClick={() => router.push("/main")}>Trenuci za pamcenje</h1>
                <div className="mx-48" />
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border"  onClick={() => router.push("/offers")}>PONUDE</div>
                <div className="bg-pink-selected px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border">OBAVEŠTENJA</div>
                <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border-1 border-pink-border"  onClick={() => router.push("/events")}>DOGAĐAJI</div>
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
            <h1 className="text-center text-gray-900 font-lunasima text-5xl py-6 my-6">OBAVEŠTENJA</h1>
            <div className="flex flex-col items-center gap-4">

            {notifications.map((notification) => (
                <div key={notification._id} className="px-6"> {AppointmentCard(notification)} </div>
      ))} </div>
    </div>)
}
function AppointmentCard(appointment: Appointment) {


  const accepted = appointment.status === "accepted";
  const rejected = appointment.status === "rejected";

  return (
    <div className="bg-white rounded-md shadow-sm border p-4 flex gap-4 items-start max-w-xl">
      <div className="flex-shrink-0">
        {accepted && (
          <div className="h-12 w-12 rounded-full bg-pink-300 flex items-center justify-center">
            <svg viewBox="0 0 24 24" className="h-7 w-7" fill="none" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
              <path d="M20 6L9 17l-5-5" />
            </svg>
          </div>
        )}
        {rejected && (
          <div className="h-12 w-12 rounded-full bg-gray-400 flex items-center justify-center">
            <svg viewBox="0 0 24 24" className="h-7 w-7" fill="none" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
              <path d="M6 6l12 12M18 6L6 18" />
            </svg>
          </div>
        )}
      </div>

      <div className="flex-1 w-100">
        {accepted && (
          <p className="text-sm mb-2 text-black">
            Zakazivanje je uspešno!<br />
            Vaš događaj za {appointment.date} je potvrđen od strane organizatora.
          </p>
        )}
        {rejected && (
          <p className="text-sm mb-2 text-black">
            Nažalost, Vaš događaj za {appointment.date} je odbijen od strane organizatora.
          </p>
        )}

        <div className="flex gap-3 items-center">
          <img
            src={appointment.offer.offerImageUrl}
            alt={appointment.title}
            className="h-20 w-20 object-cover rounded-sm"
          />
          <div className="text-black">
            <p className="text-sm">{appointment.offer.name} (sala {appointment.offer.hall.name})</p>
            <p className="text-sm text-gray-700">
              {appointment.guests} gostiju, {appointment.date}
            </p>
            <p className="text-2xl font-semibold mt-1">{appointment.guests * appointment.offer.price} €</p>
          </div>
        </div>
      </div>
    </div>
  );
}