"use client";
import { UserAPI } from "@/services/api";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";
import { useRouter } from "next/navigation";
import { Router } from "next/router";
import { useEffect, useState } from "react";
import { AxiosError } from "axios";
import { User } from "@/types/User";
import { ErrorMessages } from "@/constants/errorMessages";

export default function Home() {
  console.log("a")

  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [emailError, setEmailError] = useState(false) 
  const [passwordError, setPasswordError] = useState(false)
  const [error, setError] = useState("")
  const router = useRouter()
  const userJson =
    typeof window !== "undefined"
      ? localStorage.getItem("user")
      : null
  const user: User = userJson!= null ? JSON.parse(userJson): null
  if (user!=null) router.push(user.role === "buyer"? "/main":"main_organizer")
  const validate = () => {
    return (email!=null && email.length !=0 && password!=null && password.length != 0)
  }
  const handleLogin = async ()=>{
    try{
      console.log("Handle login")
      const res = await UserAPI.login({email, password})
      localStorage.setItem("user", JSON.stringify(res.data))
      if(res.data.role == "buyer")
      router.push("/main")
      else 
      router.push("main_organizer")
    }
    catch(err){ 
      const axiosError = err as AxiosError<User>
      if(axiosError.response?.status === 400) setError(ErrorMessages.wrongCredentials)
        else setError(ErrorMessages.unknownServerError)
    }
  }
  useEffect(()=>{
       
    })
return (
<div className="min-h-screen bg-brand bg-no-repeat bg-right bg-contain [background-image:url('/images/login_pozadina.png')] flex items-center">
      <div className="ml-96 w-96 h-100 bg-brand rounded-2xl shadow-lg p-2 border-2 border-pink-stroke">
        <h1 className="text-4xl font-inter text-center text-black pt-6 ">Dobro došli</h1>
        <div className="h-8" />
        <div className="flex items-start gap-3">
          <img
            src="/images/logovanje_korisnicko_ime.png"
            className="mt-6 h-8 w-8 ml-2"
          />
          <div className="flex-1">
          <label className="block text-xs text-black/80 mb-1 ml-3">Email adresa</label>
          <input
            type="email"
            value = {email}
            onChange={(e) => setEmail(e.target.value)} 
            placeholder="Email adresa"
            className="w-64 h-10 rounded-lg  bg-transparent
                      px-3 text-black placeholder-black/40
                      focus:outline-none focus:ring-2 focus:ring-brand/60 focus:border-brand border-1  border-pink-stroke"
          />
        </div>
        </div>
        <div className="h-4" />
        <div className="flex items-start gap-3">
          <img
            src="/images/logovanje_lozinka.png"
            className="mt-6 h-8 w-8 ml-2"
          />
          <div>
          <label className="block text-xs text-black/80 mb-1 ml-3">Lozinka</label>
          <input
            type="password"
            value = {password}
            onChange={(e) => setPassword(e.target.value)} 
            placeholder="Lozinka"
            className="w-64 h-10 rounded-lg  bg-transparent
                      px-3 text-black placeholder-black/40
                      focus:outline-none focus:ring-2 focus:ring-brand/60 focus:border-brand border-1  border-pink-stroke"
          />
        </div>
        </div>
        <h1 className="font-inter text-error-text text-center pt-2.5">{error}</h1>
        <div className="h-8" />
         <div className="flex justify-center">
          <button type= "button" className="bg-pink-button hover:bg-purple-600 text-white font-inter py-2 px-6 rounded-md shadow" onClick={handleLogin}>
            Prijavite se
          </button>
        </div>
        
      </div>
  </div>
);

}
