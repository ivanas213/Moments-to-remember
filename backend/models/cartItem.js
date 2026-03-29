import mongoose, { Mongoose } from 'mongoose' 

const cartSchema = new mongoose.Schema({
    user:{
        type: mongoose.Schema.Types.ObjectId,
        required: true,
        ref: 'User'
    },
    offer:{
        type: mongoose.Schema.Types.ObjectId,
        required: true,
        ref: 'Offer'
    },
    guests: {
        type: Number,
        required: true
    }
})
export default mongoose.model("CartItem", cartSchema)