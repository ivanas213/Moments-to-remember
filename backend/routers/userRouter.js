import express from 'express';
const router = express.Router();
import User from "../models/user.js"

router.post("/login", async (req, res)=>{
    console.log("Usli smo u login")
    try{
        const {email, password} = req.body
        const user = await User.findOne({email: email, password: password})
        if(!user) return res.status(400).json({"message": "Invalid credentials"})
            return res.status(200).json(user)
    }
    catch(error){
        console.log(error)
        return res.status(500).json({"message": "Internal server error "+ error})
    }
})
export default router