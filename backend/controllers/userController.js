import User from "../models/user.js"

const login = async (req, res)=>{
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
}
const updateDataById = async (req, res) => {
    const userId = req.params.id
    const updatedData = req.body

    try {
        const user = await User.findByIdAndUpdate(userId, updatedData, { new: true });
        if (!user) return res.status(404).send("User not found");
        res.send(user); 
    } catch (err) {
        res.status(500).send("Error updating user");
    }
}

export {login, updateDataById}