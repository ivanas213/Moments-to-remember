"use client";

import { AppointmentAPI, EventAPI, HallAPI, OfferAPI, PromotionAPI } from "@/services/api";
import { Promotion } from "@/types/Promotion";
import { Event } from "@/types/Event";
import Image from "next/image";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Hall } from "@/types/Hall";
import { AddOfferRequest } from "@/types/AddOfferRequest";
import { Appointment } from "@/types/Appointment";
import { User } from "@/types/User";
import { FaLastfmSquare } from "react-icons/fa";

type action = "accept" | "reject";



export default function Main() {
  const [promotions, setPromotions] = useState<Promotion[]>([]);
  const [halls, setHalls] = useState<Hall[]>([]);
  const [events, setEvents] = useState<Event[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [pending, setPending] = useState<Appointment[]>([]);
 
  const router = useRouter();

  useEffect(() => {
    const userJson = typeof window !== "undefined" ? localStorage.getItem("user") : null;
    const user: User | null = userJson ? JSON.parse(userJson) : null;
    if (!user || user.role !== "organiser") router.push("/");
  }, [router]);

  useEffect(() => {
    const fetchPromotions = async () => {
      try {
        const res = await PromotionAPI.getPromotions();
        setPromotions(res.data);
      } catch (err) {
        console.error("Greška:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchPromotions();
  }, []);

  useEffect(() => {
    const fetchPending = async () => {
      try {
        const res = await AppointmentAPI.getPending();
        setPending(res.data);
      } catch (err) {
        console.error("Greška:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchPending();
  }, []);

  useEffect(() => {
    const fetchHalls = async () => {
      try {
        const res = await HallAPI.getAllHalls();
        setHalls(res.data);
      } catch (err) {
        console.error("Greška:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchHalls();
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
      setCurrentIndex((prev) => (prev === promotions.length - 1 ? 0 : prev + 1));
    }, 20000);
    return () => clearInterval(interval);
  }, [promotions]);

  const handleDecision = async (appointmentId: string, action: action) => {
    const prev = pending;
    const target = prev.find((p) => p._id === appointmentId);
    if (!target) return;

    setPending((list) => list.filter((p) => p._id !== appointmentId));

    try {
      if (action === "accept") {
        await AppointmentAPI.accept(appointmentId);
      } else {
        await AppointmentAPI.reject(appointmentId);
      }
    } catch (e) {
      console.error(e);
      setPending(prev);
    } finally {
    
    }
  };

  return (
    <div className="min-h-screen bg-brand">
      <header className="h-12 bg-pink-header flex items-center justify-between px-3">
        <div className="flex items-center gap-2">
          <img src="/images/logo.png" className="h-8 w-8" alt="Logo" />
          <h1 className="text-gray-950 font-italianno text-xl">Trenuci za pamćenje</h1>
        </div>

        <div className="flex items-center gap-2" onClick={() => router.push("/profile")}>
          <span className="text-black">Profil</span>
          <img src="/images/profil.png" className="h-6 w-6" alt="Profil" />
        </div>
      </header>

      {AddEventForm()}

      <hr className="border-black" />

      <h1 className="text-center text-2xl md:text-3xl font-bold text-black py-3">ZAHTEVI</h1>

      <div className="space-y-3 px-6 pb-10">
        {pending.map((p) => (
          <AppointmentCard
            key={p._id}
            appointment={p}
            onDecision={handleDecision}
          />
        ))}
        {pending.length === 0 && (
          <p className="text-center text-black/70">Trenutno nema zahteva na čekanju.</p>
        )}
      </div>

      {/* {msg && (
        <div className="fixed bottom-4 left-1/2 -translate-x-1/2 bg-black text-white px-4 py-2 rounded-md shadow">
          {msg}
        </div>
      )} */}
    </div>
  );
}

function AddEventForm() {
  const [file, setFile] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);

  const [name, setName] = useState("");
  const [price, setPrice] = useState<number | "">("");
  const [min, setMin] = useState<number | "">("");
  const [max, setMax] = useState<number | "">("");

  const [halls, setHalls] = useState<Array<{ _id: string; name: string }>>([]);
  const [hallId, setHallId] = useState("");

  const [submitting, setSubmitting] = useState(false);

  function handleFileChange(e: React.ChangeEvent<HTMLInputElement>) {
    const f = e.target.files?.[0];
    if (!f) return;
    setFile(f);
    setPreview(URL.createObjectURL(f));
  }

  useEffect(() => {
    (async () => {
      try {
        const res = await HallAPI.getAllHalls();
        setHalls(res.data);
        if (res.data?.length) setHallId(res.data[0]._id);
      } catch (e) {
      }
    })();
  }, []);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    if (!file) return alert("Izaberi sliku.");
    if (!name.trim()) return alert("Unesi naziv.");
    if (price === "" || min === "" || max === "") return alert("Unesi cenu, min i max.");
    if (Number(min) > Number(max)) return alert("Min broj gostiju ne može biti veći od max.");
    if (!hallId) return alert("Izaberi salu.");

    const request: AddOfferRequest = {
      image: file,
      hall: hallId,
      maxGuests: max as number,
      minGuests: min as number,
      name,
      price: price as number,
    };

    try {
      setSubmitting(true);
      await OfferAPI.add(request);

      if (preview) URL.revokeObjectURL(preview);
      setFile(null);
      setPreview(null);
      setName("");
      setPrice("");
      setMin("");
      setMax("");
    } catch (err) {
      console.error(err);
      alert("Greška prilikom čuvanja.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="min-h-screen">
      <div className="mx-auto max-w-5xl px-4 py-10">
        <h1 className="text-center text-2xl md:text-3xl font-bold text-black">DODAVANJE NOVOG DOGAĐAJA</h1>

        <form onSubmit={handleSubmit}>
          <div className="mt-10 grid grid-cols-1 md:grid-cols-12 gap-6 items-start">
            <label htmlFor="event-image" className="md:col-span-3 flex flex-col items-center cursor-pointer">
              <div className="w-40 h-40 bg-white rounded-sm shadow-sm border border-black/10 flex items-center justify-center overflow-hidden">
                {preview ? (
                  <img src={preview} alt="Preview" className="w-full h-full object-cover" />
                ) : (
                  <span className="text-gray-500 text-sm">Izaberi sliku</span>
                )}
              </div>
              <span className="mt-2 text-sm text-gray-800">Izaberi sliku</span>
            </label>
            <input id="event-image" type="file" accept="image/*" onChange={handleFileChange} className="hidden" />

            <label className="md:col-span-4 flex flex-col items-center">
              <input
                className="text-black w-full h-12 bg-white rounded-sm shadow-sm border border-black/10 px-3 outline-none focus:ring-2 focus:ring-pink-300"
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              <span className="mt-2 text-sm text-gray-800">Naziv</span>
            </label>

            <label className="md:col-span-2 flex flex-col items-center">
              <input
                className="text-black w-full h-12 bg-white rounded-sm shadow-sm border border-black/10 px-3 outline-none focus:ring-2 focus:ring-pink-300"
                type="number"
                min={0}
                value={price}
                onChange={(e) => setPrice(e.target.value === "" ? "" : Number(e.target.value))}
              />
              <span className="mt-2 text-sm text-gray-800">Cena po gostu</span>
            </label>

            <div className="md:col-span-3 grid grid-cols-2 gap-4">
              <label className="flex flex-col items-center">
                <input
                  className="text-black w-full h-12 bg-white rounded-sm shadow-sm border border-black/10 px-3 outline-none focus:ring-2 focus:ring-pink-300"
                  type="number"
                  min={0}
                  value={min}
                  onChange={(e) => setMin(e.target.value === "" ? "" : Number(e.target.value))}
                />
                <span className="mt-2 text-sm text-gray-800">min broj gostiju</span>
              </label>
              <label className="flex flex-col items-center">
                <input
                  className="text-black w-full h-12 bg-white rounded-sm shadow-sm border border-black/10 px-3 outline-none focus:ring-2 focus:ring-pink-300"
                  type="number"
                  min={0}
                  value={max}
                  onChange={(e) => setMax(e.target.value === "" ? "" : Number(e.target.value))}
                />
                <span className="mt-2 text-sm text-gray-800">max broj gostiju</span>
                <span className="mt-2 text-sm text-gray-800">&nbsp;</span>
              </label>
            </div>
          </div>

          <div className="mt-6 grid grid-cols-12 gap-6 items-end">
            <label className="col-span-12 md:col-span-3 flex flex-col items-center">
              <select
                className="text-black w-full h-12 bg-white rounded-sm shadow-sm border border-black/10 px-3 outline-none focus:ring-2 focus:ring-pink-300"
                value={hallId}
                onChange={(e) => setHallId(e.target.value)}
              >
                {halls.length === 0 ? (
                  <option value="" disabled>
                    Učitavanje sala...
                  </option>
                ) : (
                  halls.map((h) => (
                    <option key={h._id} value={h._id}>
                      {h.name}
                    </option>
                  ))
                )}
              </select>
              <span className="mt-2 text-sm text-gray-800">Sala</span>
            </label>

            <div className="col-span-12 md:col-span-2 flex flex-col items-center gap-3">
              <button
                type="submit"
                disabled={submitting}
                className={`w-20 h-20 rounded-full flex items-center justify-center shadow-sm border border-black/5 transition
                 ${submitting ? "bg-green-300 cursor-not-allowed" : "bg-green-200 hover:scale-105"}`}
                title="Dodaj događaj"
              >
                {submitting ? (
                  <div className="animate-spin w-8 h-8 border-4 border-white border-t-transparent rounded-full" />
                ) : (
                  <svg viewBox="0 0 24 24" className="w-10 h-10" fill="none" stroke="green" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M5 12h14M12 5l7 7-7 7" />
                  </svg>
                )}
              </button>
              <span className="text-sm text-gray-800">Dodaj događaj</span>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

function AppointmentCard({
  appointment,
  onDecision,
}: {
  appointment: Appointment;
  onDecision: (id: string, action: action) => Promise<void>;
}) {
  return (
    <div className="w-full bg-[rgb(247,226,232)] rounded-md p-3">
      <div className="flex items-center gap-4">
        <img src={appointment.offer.offerImageUrl} alt="" className="h-16 w-16 rounded-sm object-cover" />

        <div className="flex-1 text-black">
          <p className="text-sm">Email adresa: {appointment.user.username}</p>
          <p className="text-sm">
            {appointment.offer.name} <span className="text-gray-700">(sala {appointment.offer.hall.name})</span>
          </p>
          <p className="text-sm text-gray-700">
            broj gostiju: {appointment.guests}, {appointment.date}
          </p>
          <p className="text-2xl font-semibold mt-1">{appointment.guests * appointment.offer.price} €</p>
        </div>

        <div className="flex items-center gap-6 pr-1">
          <div className="flex flex-col items-center gap-1">
            <button
              type="button"
              onClick={() => onDecision(appointment._id, "accept")}
              className={`h-9 w-9 rounded-full flex items-center justify-center shadow-sm border `}
              aria-label="Prihvati"
              title="Prihvati"
            >
              <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" stroke="green" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
                <path d="M20 6L9 17l-5-5" />
              </svg>
            </button>
            <span className="text-xs text-black">Prihvati</span>
          </div>

          <div className="flex flex-col items-center gap-1">
            <button
              type="button"
              onClick={() => onDecision(appointment._id, "reject")}
              className={`h-9 w-9 rounded-full flex items-center justify-center shadow-sm border`}
              aria-label="Odbij"
              title="Odbij"
            >
              <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" stroke="black" strokeWidth="3" strokeLinecap="round">
                <path d="M6 6l12 12M18 6L6 18" />
              </svg>
            </button>
            <span className="text-xs text-black">Odbij</span>
          </div>
        </div>
      </div>
    </div>
  );
}
