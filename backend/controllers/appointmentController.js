import Appointment from '../models/appointment.js'


const getProcessedAppointments = async (req, res) => {
  try {
    const appointments = await Appointment.find({
      status: { $in: ['accepted', 'rejected' ]},
    }).populate({
      path: 'offer',
      populate: {
        path: 'hall' 
      }
    });

    res.status(200).json(appointments);
  } catch (err) {
    res.status(500).json({ message: 'Server error', error: err.message });
  }
};
const add = async (req, res) => {
    try {
        const appointment = new Appointment(req.body);
        await appointment.save(); 
        await appointment.populate({
          path: 'offer',
          populate: { path: 'hall' }
        }).populate({ path: 'user' })

        res.json(appointment); 
    } catch (ex) {
        res.status(500).json({ error: ex.message });
    }
};

const getCartForUser = async(req, res)=>{
    const userId = req.params.userId
    try {
      const cartAppointments = await Appointment.find({
      user: userId,
      status: 'cart'
    })
    .populate({
      path: 'offer',
      populate: { path: 'hall' }
    })
    .populate({ path: 'user' })

      res.status(200).json(cartAppointments);
    } catch (ex) {
      console.log("Error", ex.message)
      res.status(500).json({ message: ex.message });
    }
}
const deleteAppointment = async (req, res) =>{
  try{
    const id = req.params.appointmentId
    const deletedItem = await Appointment.findByIdAndDelete(id)
    if (!deletedItem) res.status(404).json({message: "Id not found"})
    else{
      res.status(200).json({})
    }
  
  }
  catch(ex){
    res.status(500).json({ message: ex.message });
  }
    
}
const sendRequest = async (req, res) => {
  try{
    const id = req.params.id
    const appointment = Appointment.findByIdAndUpdate(
      id,
      {status: "pending"},
      {new: true}
    )
    if (!appointment) res.status(404).json({message: "Appointment not found"})
    else res.status(200)
  }
  catch(ex){
    res.status(500).json({ message: ex.message });
  }
}
const acceptRequest = async (req, res) => {
  try{
    const {appointmentId} = req.params
    const appointment = await Appointment.findByIdAndUpdate(
      appointmentId,
      {status: "accepted"},
      {new: true}
    )
    if (!appointment) res.status(404).json({message: "Appointment not found"})
    else res.status(200).json()
  }
  catch(ex){
    res.status(500).json({ message: ex.message });
  }
}
const rejectRequest = async (req, res) => {
  try{
    const {appointmentId} = req.params
    const appointment = await Appointment.findByIdAndUpdate(
      appointmentId,
      {status: "rejected"},
      {new: true}
    )
    if (!appointment) res.status(404).json({message: "Appointment not found"})
    else res.status(200).json()
  }
  catch(ex){
    res.status(500).json({ message: ex.message });
  }
}

const appoint = async (req, res) => {
  const userId = req.body.userId;

  try {
    const result = await Appointment.updateMany(
      { user: userId, status: "cart" },
      { $set: { status: "pending" } }
    );


    const updatedAppointments = await Appointment.find({
      user: userId,
      status: "pending"
    })
      .populate({
        path: "offer",
        populate: { path: "hall" }
      })
      .populate({ path: "user" });

    res.status(200).json({
    });
  } catch (ex) {
    console.log("Error", ex.message);
    res.status(500).json({ message: ex.message });
  }
};

const getPendingAppointments = async (req, res) => {
  try {
    const appointments = await Appointment.find({
      status: { $in: ['pending' ]} 
    }).populate({
      path: 'offer',
      populate: {
        path: 'hall' 
      }
    });

    res.status(200).json(appointments);
  } catch (err) {
    res.status(500).json({ message: 'Server error', error: err.message });
  }
};
const getNotificationsForUser = async (req, res) => {
  const userId = req.params.userId
  try {
    const appointments = await Appointment.find({
      user: userId,
      status: { $in: ['accepted', 'rejected'] }
    })
      .populate({
        path: 'offer',
        populate: { path: 'hall' }
      })
      .populate({ path: 'user' })

    const result = appointments.map(doc => doc.toObject());

    res.status(200).json(result);

    await Appointment.updateMany(
      { _id: { $in: appointments.map(a => a._id) } },
      { $set: { new: false } }
    );

  } catch (ex) {
    console.log("Error", ex.message)
    res.status(500).json({ message: ex.message });
  }
}

export { getProcessedAppointments, add, getCartForUser, deleteAppointment, sendRequest, acceptRequest, rejectRequest, appoint, getPendingAppointments, getNotificationsForUser };
