import mongoose from 'mongoose' 

const appointmentSchema = new mongoose.Schema({
  status: {
    type: String,
    enum: ['accepted', 'rejected', 'pending', 'cart'],
    default: 'pending',
    required: true
  },
  new: {
    type: Boolean,
    default: true
  },
  date: {
    type: String,
    required: true
  },
  offer: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Offer',
    required: true
  },
  user:{
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    ref: 'User'
  },
  guests: {
        type: Number,
        required: true
  }
}, {
  timestamps: true
});

export default mongoose.model('Appointment', appointmentSchema);
