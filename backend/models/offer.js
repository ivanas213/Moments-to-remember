import mongoose from 'mongoose'

const offerSchema = new mongoose.Schema({
    offerImageUrl: {type:String, required:true},
    name: {type:String, required:true},
    minGuests: {type:Number, required:true},
    maxGuests: {type:Number, required:true},
    price: {type:Number, required:true},
    hall: {type: mongoose.Schema.Types.ObjectId, ref: 'Hall', required:true }
})

export default mongoose.model("Offer", offerSchema)