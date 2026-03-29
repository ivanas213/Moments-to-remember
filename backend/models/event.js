import mongoose from 'mongoose';

const eventSchema = new mongoose.Schema({
    imageUrl: {type:String, required:true},
    title:{type:String, required:true},
    date: {type:String, required:true},
    description:{type:String, required:true}
})

export default mongoose.model("Event", eventSchema)