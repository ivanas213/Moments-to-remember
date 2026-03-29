import mongoose from "mongoose";

const hallSchema = new mongoose.Schema({
    name: {type:String, required:true}, 
    imageUrl: {type:String, required:true},
    capacity: {type:Number, required:true},
    address: {type:String, required:true}, 
    description: {type:String, required:true}
})

export default mongoose.model("Hall", hallSchema)