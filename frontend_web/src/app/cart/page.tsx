"use client"

import { AppointmentAPI } from "@/services/api"
import { Appointment } from "@/types/Appointment";
import { AppointRequest } from "@/types/AppointRequest";
import { User } from "@/types/User";
import { useRouter } from "next/navigation";
import { useEffect, useMemo, useState } from "react"

export default function Main(){
  const [loading, setLoading] = useState(true)
  const [myCart, setMyCart] = useState<Appointment[]>([])
  const userJson = typeof window !== "undefined" ? localStorage.getItem("user") : null
  const user: User | null = userJson ? JSON.parse(userJson) : null
  const router = useRouter()

  useEffect(() => {
    if (!user || user.role !== "buyer") {
      router.replace("/")
    }
  }, [])

  useEffect(() => {
    const fetchCart = async () => {
      try{
        if (!user?._id) return
        const res = await AppointmentAPI.getCart(user._id)
        setMyCart(res.data)
      } catch (err) {
        console.error("Greška:", err)
      } finally {
        setLoading(false)
      }
    }
    fetchCart()
  }, [user?._id])

  const sum = useMemo(() => {
    return myCart.reduce((acc, a) => acc + a.guests * a.offer.price, 0)
  }, [myCart])

  const handleRemove = async (id: string) => {
    setMyCart(prev => prev.filter(a => a._id !== id))
    try {
      await AppointmentAPI.deleteAppointment(id)
      
    } catch (err) {
      console.error("Greška pri brisanju:", err)
      setMyCart(prev => prev) 
     
    }
  }

  const handleAppoint = async () => {
    if (!user?._id || myCart.length === 0) return
    try {
      const request: AppointRequest = { userId: user._id }
      await AppointmentAPI.appoint(request)
      setMyCart([])
      router.refresh()
    } catch (err) {
      console.error("Greška pri zakazivanju:", err)
    }
  }

  if (!user || user.role !== "buyer") {
    return null
  }

  return (
    <div className="min-h-screen bg-brand">
      <div className="h-12 bg-pink-header flex items-center">
        <img
          onClick={() => router.push("/main")}
          src="images/logo.png"
          className="mt-2 h-14 w-14 ml-2 cursor-pointer"
        />
        <div className="mx-1" />
        <h1
          className="text-black font-italianno font-base text-gray-950 text-2xl cursor-pointer"
          onClick={() => router.push("/main")}
        >
          Trenuci za pamćenje
        </h1>
        <div className="mx-48" />
        <div
          className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border border-pink-border cursor-pointer"
          onClick={() => router.push("/offers")}
        >
          PONUDE
        </div>
        <div
          className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border border-pink-border cursor-pointer"
          onClick={() => router.push("/events")}
        >
          OBAVEŠTENJA
        </div>
        <div
          className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border border-pink-border cursor-pointer"
          onClick={() => router.push("/notifications")}
        >
          DOGAĐAJI
        </div>
        <div className="mx-6" />
        <img
          src="images/korpa_izabrana.png"
          className="mt-2 h-8 w-8 ml-2"
        />
        <div className="mx-1" />
        <img
          src="images/profil.png"
          className="mt-2 h-8 w-8 ml-2 cursor-pointer"
          onClick={() => router.push("/profile")}
        />
      </div>

      <div className="min-h-screen flex items-center justify-center w-full">
        <div className="bg-white rounded-2xl shadow-lg w-[25rem] relative mx-auto p-6">
          <div className="flex flex-col items-center">
            <img src="/images/korpa_prikaz.png" alt="User" className="w-12 h-12" />
            <h2 className="text-xl font-bold text-black mt-2">Korpa</h2>
            <hr className="border-black my-4 w-full" />
          </div>

          {loading ? (
            <div className="text-center text-gray-600 py-6">Učitavanje…</div>
          ) : myCart.length === 0 ? (
            <div className="text-center text-gray-700 py-6">Korpa je prazna.</div>
          ) : (
            <div className="flex flex-col gap-3">
              {myCart.map((appointment) => (
                <AppointmentCard
                  key={appointment._id}
                  appointment={appointment}
                  onRemove={() => handleRemove(appointment._id)}
                />
              ))}
            </div>
          )}

          <div className="h-5" />
          <h1 className="text-black text-center text-4xl">{sum} €</h1>
          <div className="h-5" />
          <div className="flex justify-center">
            <button
              className="text-black bg-pink-light px-5 py-2 rounded-xl disabled:opacity-50 disabled:cursor-not-allowed"
              disabled={myCart.length === 0}
              onClick={handleAppoint}
            >
              Zakaži događaje
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

function AppointmentCard({
  appointment,
  onRemove,
}: {
  appointment: Appointment
  onRemove: () => void
}) {
  return (
    <div className="flex items-center gap-4 bg-white rounded-xl shadow-sm p-3 border">
      <img
        src={appointment.offer.offerImageUrl}
        alt=""
        className="w-20 h-20 rounded-md object-cover"
      />

      <div className="flex-1">
        <div className="font-semibold text-gray-900">
          {appointment.offer?.name} {`(sala ${appointment.offer.hall.name})`}
        </div>
        <div className="text-sm text-gray-600">
          broj gostiju: {appointment.guests}, {appointment.date}
        </div>
        <div className="text-2xl font-bold mt-1 text-black">
          {appointment.guests * appointment.offer.price} €
        </div>
      </div>

      <button
        onClick={onRemove}
        className="flex flex-col items-center text-gray-700 hover:text-red-600"
        aria-label="Ukloni"
      >
        <img src="/images/ukloni.png" alt="" className="w-8 h-8" />
        <span className="text-sm">Ukloni</span>
      </button>
    </div>
  )
}
