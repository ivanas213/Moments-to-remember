import mongoose from 'mongoose';

const promotionSchema = new mongoose.Schema({
  imageUrl: { type: String, required: true },
  description: { type: String },
});

export default mongoose.model('Promotion', promotionSchema);
