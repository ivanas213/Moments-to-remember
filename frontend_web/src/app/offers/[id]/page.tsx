"use client"

import { RatingAPI } from "@/services/api"
import { Offer } from "@/types/Offer"
import { useParams, useRouter } from "next/navigation"
import { useEffect, useState } from "react"
import Image from "next/image"
import { Rating } from "@/types/Rating"
import { FaStar } from "react-icons/fa";
import { AddRatingRequest } from "@/types/AddRatingRequest"
import { User } from "@/types/User"

export default function Main(){
  const { id } = useParams<{ id: string }>();
  const [rates, setRates] = useState<Rating[]>([])
  const [averageRate, setAverageRate] = useState(0)
  const [loading, setLoading] = useState(true)

  const offersJson = typeof window !== "undefined" ? localStorage.getItem("offers") : null
  const offers: Offer[] = offersJson ? JSON.parse(offersJson) : []
  const myOffer = offers?.find(o => o._id === id)
  const [added, setAdded] = useState(false)
  const userJson = typeof window !== "undefined" ? localStorage.getItem("user") : null
  const user: User | null = userJson ? JSON.parse(userJson) : null

  const router = useRouter()
  useEffect(() => {
    if (!user || user.role !== "buyer") router.replace("/")
  }, [])

  const setAverage = (list: Rating[]) => {
    if (list.length == 0) { setAverageRate(0); return }
    let sum = 0
    list.forEach(element => {
      sum += element.value
    });
    setAverageRate(sum / list.length)
  }

  const fetchRates = async () => {
    try {
      if (!myOffer?._id) return
      const res = await RatingAPI.getAllRatings(myOffer._id)
      const reversed = [...res.data].reverse()
      setRates(reversed)
      setAverage(reversed)
    } catch (err) {
      console.error("Greška:", err)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    setLoading(true)
    fetchRates()
  }, [myOffer?._id])

  const onAddRating = async (value: number, comment: string) => {
  if (!user || !myOffer?._id) return

  try {
    const request: AddRatingRequest = {
      user: user._id,
      offer: myOffer._id,
      value,
      comment,
    }

    const res = await RatingAPI.addRating(request)
    const saved: Rating | undefined = res?.data

    if (saved && saved._id) {
      setRates(prev => {
        let found = false
        let rate = 0
        const next = prev.map(r => {
          if (r.user._id === user._id && r.offer._id === myOffer._id) {
            found = true
            rate = r.value 
            return saved
          }
          return r
        })
        if (!found) next.unshift(saved) 
        let sum = 0
        rates.forEach(element => {
          sum += element.value
        });
        if(found)
        setAverageRate((sum + value - rate) / rates.length)
        else setAverageRate((sum + value)/ (rates.length + 1))
            return next
          })
    }
  } catch (err) {
    console.error("Greška pri čuvanju ocene:", err)
  }
}

  

  if (!user || user.role !== "buyer") return null

  return (
    <div className="min-h-screen bg-brand ">
      <div className="h-12 bg-pink-header flex items-center">
        <img
          src="../images/logo.png"
          className="mt-2 h-14 w-14 ml-2 cursor-pointer"
          onClick={() => router.push("/main")}
        />
        <div className="mx-1" />
        <h1 className="text-black font-italianno font-base text-gray-950 text-2xl cursor-pointer" onClick={() => router.push("/main")}>Trenuci za pamćenje</h1>
        <div className="mx-48" />
        <div className="bg-pink-selected px-12 py-2 rounded-t-2xl font-lato text-sm self-end border border-pink-border">PONUDE</div>
        <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border border-pink-border cursor-pointer"  onClick={() => router.push("/notifications")}>OBAVEŠTENJA</div>
        <div className="bg-pink-menu-item px-12 py-2 rounded-t-2xl font-lato text-sm self-end border border-pink-border cursor-pointer"  onClick={() => router.push("/events")}>DOGAĐAJI</div>
        <div className="mx-6" />
        <img src="../images/korpa.png" className="mt-2 h-8 w-8 ml-2 cursor-pointer" onClick={() => router.push("/cart")} />
        <div className="mx-1" />
        <img src="../images/profil.png" className="mt-2 h-8 w-8 ml-2 cursor-pointer" onClick={() => router.push("/profile")} />
      </div>

      <div className="h-9" />
      <div className="flex flex-col items-center min-w-screen">
        <h1 className="text-center text-gray-900 font-lunasima text-5xl ">
          {myOffer?.name} (sala {myOffer?.hall.name})
        </h1>
        <h1 className="h-10"/>
        <Image
          src={myOffer ? myOffer.hall.imageUrl : ""}
          alt=""
          width={1000}
          height={500}
        />
        <div className="h-4" />
        <h2 className="text-black font-cormorant-garamond text-2xl px-12">{myOffer?.hall.description}</h2>
        <h1 className="h-10"/>
      </div>

      <hr className=" border-black" />
      <div className="font-bold">
        <h1 className="h-6"/>
        <h1 className="font-nunito text-black ps-12 text-lg" >Uslovi pri zakazivanju:</h1>
        <h1 className="h-6"/>
        <h1 className="font-nunito text-black ps-12" >Minimalan broj gostiju: {myOffer?.minGuests}</h1>
        <h1 className="font-nunito text-black ps-12" >Maksimalan broj gostiju: {myOffer?.maxGuests}</h1>
        <h1 className="font-nunito text-black ps-12" >Cena po gostu: {myOffer?.price}€</h1>
      </div>

      <h1 className="h-6"/>
      <button className="rounded-md bg-gray-200 mx-12 px-2 py-2 text-sm font-medium shadow-sm hover:bg-gray-300 text-black" onClick={() => router.push(`/offers/${id}/appointment`)}>Zakaži svoj termin</button>
      <h1 className="h-6"/>
      <hr className=" border-black" />

      {RatingStars(averageRate, rates.length)}
      <h1 className="h-4"/>

      <div className="bg-white mx-10">
        <RateBox
          user={user}
          offerId={myOffer?._id ?? ""}
          onAdd={onAddRating}        />

        {loading ? (
          <div className="p-4 text-gray-600">Učitavanje ocena…</div>
        ) : (
          rates.map((rate) => (
            <div key={rate._id}>{Rate(rate)}</div>
          ))
        )}
      </div>
    </div>
  )
}

function RatingStars(rating: number, count:number) {
  return (
    <div className="flex items-center pl-9">
      <div className="relative flex">
        <div className="flex">
          {Array.from({ length: 5 }).map((_, i) => (
            <span key={i} className="text-gray-300 text-3xl">★</span>
          ))}
        </div>
        <div
          className="flex absolute top-0 left-0 overflow-hidden text-yellow-400 text-3xl"
          style={{ width: `${(rating / 5) * 100}%` }}
        >
          {Array.from({ length: 5 }).map((_, i) => (
            <span key={i} className="text-3xl">★</span>
          ))}
        </div>
      </div>
      <div className="text-black ml-3">{Number(rating).toFixed(2)} (broj ocena: {count})</div>
    </div>
  );
}

function Rate(rate: Rating) {
  return (
    <div className="p-4 border-b border-gray-200">
      <div className="flex items-center">
        <img
          src="../images/profil_komentar.png"
          className="h-6 w-6 rounded-full mr-2"
          alt="User avatar"
        />
        <span className="text-sm text-gray-700 font-semibold mr-2">
          {rate.user.first_name} {rate.user.last_name}
        </span>
        <div className="flex">
          {Array.from({ length: 5 }).map((_, i) => (
            <FaStar
              key={i}
              className={`h-4 w-4 ${i < rate.value ? "text-yellow-400" : "text-gray-300"}`}
            />
          ))}
        </div>
      </div>
      <p className="text-gray-800 text-sm mt-1 pl-8">{rate.comment}</p>
    </div>
  );
}

function RateBox({
  user,
  offerId,
  onAdd,
}: {
  user: User
  offerId: string
  onAdd: (value: number, comment: string) => Promise<void> | void
}) {
  const [rating, setRating] = useState(0);
  const [hover, setHover] = useState<number | null>(null);
  const [comment, setComment] = useState("");
  const [saving, setSaving] = useState(false)

  const visible = hover ?? rating;

  const handleKey = (e: React.KeyboardEvent<HTMLDivElement>) => {
    if (e.key === "ArrowRight") setRating((r) => Math.min(5, r + 1));
    if (e.key === "ArrowLeft") setRating((r) => Math.max(0, r - 1));
    if (e.key === " " || e.key === "Enter") e.preventDefault();
  };

  const handleSave = async () => {
    if (rating === 0 || !comment.trim()) return
    try {
      setSaving(true)
      await onAdd(rating, comment.trim())
      setComment("")
      setRating(0)
    } catch {
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="w-full rounded-xl bg-white/90 p-4 shadow-sm">
      <label className="block text-lg font-medium text-black mb-2">Oceni</label>

      <div role="radiogroup" aria-label="Ocena" onKeyDown={handleKey} className="flex items-center gap-2">
        {Array.from({ length: 5 }).map((_, idx) => {
          const value = idx + 1;
          const active = value <= visible;
          return (
            <button
              key={value}
              type="button"
              role="radio"
              aria-checked={rating === value}
              onMouseEnter={() => setHover(value)}
              onMouseLeave={() => setHover(null)}
              onFocus={() => setHover(value)}
              onBlur={() => setHover(null)}
              onClick={() => setRating(value)}
              className="p-1 outline-none focus:ring-2 focus:ring-pink-300 rounded-md"
              title={`${value} ${value === 1 ? "zvezdica" : "zvezdice"}`}
            >
              <FaStar className={`h-6 w-6 transition-colors ${active ? "text-yellow-400" : "text-gray-300"}`} />
            </button>
          );
        })}
      </div>

      <textarea
        placeholder="Unesi komentar"
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        className="mt-4 w-full h-28 resize-y rounded-md border border-gray-300 p-3 text-sm outline-none focus:ring-2 focus:ring-pink-300 text-black"
      />

      <div className="mt-4 flex items-center gap-3">
        <button
          type="button"
          className="rounded-md bg-gray-200 px-5 py-2 text-sm font-medium shadow-sm hover:bg-gray-300 text-black"
          onClick={() => { setComment(""); setRating(0) }}
          disabled={saving}
        >
          ODUSTANI
        </button>
        <button
          type="button"
          disabled={saving || rating === 0 || comment.trim() === ""}
          className="rounded-md bg-[#D29FCB] px-5 py-2 text-sm font-medium shadow-sm hover:brightness-95 disabled:opacity-50 disabled:cursor-not-allowed text-black"
          onClick={handleSave}
        >
          {saving ? "ČUVAM…" : "SAČUVAJ"}
        </button>
      </div>
    </div>
  );
}
