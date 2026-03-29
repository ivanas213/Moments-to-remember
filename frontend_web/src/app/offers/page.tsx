"use client"

import { OfferAPI } from "@/services/api"
import { Offer } from "@/types/Offer"
import { useRouter } from "next/navigation"
import { useEffect, useState } from "react"
import Image from "next/image"
import { Router } from "next/router"
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime"
import { User } from "@/types/User"

export default function Main(){
    const [offers, setOffers] = useState<Offer[]>([])
    const [loading, setLoading] = useState(true)
    const [currentPage, setCurrentPage] = useState(1)
    const userJson = localStorage.getItem("user")
    const user: User = userJson != null ? JSON.parse(userJson): null
    const router = useRouter()

    if(!user || user.role != "buyer") router.push("/")
    const maxPerPage = 3
    useEffect(()=>{
        if(!user || user.role != "buyer") router.push("/")
    })
    useEffect(()=>{
        const fetchOffers = async ()=>{
            try{
                const res = await OfferAPI.getOffers()
                setOffers(res.data)
                localStorage.setItem("offers", JSON.stringify(res.data))
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
        <div className="h-12 bg-pink-header flex items-center">
                <img
                    src="images/logo.png"
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
                <div className="h-12" />            
            <h1 className="text-center text-gray-900 font-lunasima text-5xl">AKTUELNE PONUDE</h1>
            <div className="h-12" />
            <div className="flex px-30 gap-24 items-center">
                {offers.slice((currentPage - 1) * maxPerPage, currentPage * maxPerPage).map((offer) => (
                OfferCard(offer, router)
                ))}
            </div>  
            <h1 className="h-4"></h1>
            <div className="flex items-center gap-3 justify-center"  >
                <button
                onClick={() => setCurrentPage(currentPage - 1)}
                disabled={currentPage == 1}
                className="text-black text-2xl disabled:opacity-50 disabled:cursor-not-allowed"
                
                >
                &laquo;
                </button>
                {Array.from({ length: Math.ceil(offers.length / 3) }, (_, i) => i + 1).map((page) => (
                <button
                key={page}
                onClick={() => setCurrentPage(page)}
                className={`w-8 h-8 rounded-full flex items-center justify-center
                    ${page === currentPage ? "bg-pink-page-selected text-white" : "bg-pink-page text-black"}`}
                >
                {page}
                </button>
                ))}

                <button
                onClick={() => setCurrentPage(currentPage + 1)}
                disabled={currentPage >= offers.length / maxPerPage}
                className="text-black text-2xl disabled:opacity-50 disabled:cursor-not-allowed"
                
                >
                &raquo;
                </button>
            </div>
    </div>)
}

function OfferCard(offer: Offer, router: AppRouterInstance) {
  return (
    <figure className=" p-3 rounded-xl w-[220px] sm:w-[260px]" onClick = {()=> router.push(`/offers/${offer._id}`)}>
      <div className=" rounded-md shadow overflow-hidden">
        <div className="relative w-full aspect-[3/4]">
          <Image
            src={offer.offerImageUrl}
            alt={offer.name}
            width={600}    
            height={400}   
            className="object-cover w-full h-80 rounded"
          />
        </div>
      </div>
      <figcaption className="text-center text-black text-sm font-cormorant-garamond bg-white">
         {offer.name} (sala {offer.hall.name})
      </figcaption>
    </figure>
  );
}