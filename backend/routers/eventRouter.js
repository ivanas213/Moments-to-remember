import express from 'express'
import Event from '../models/event.js'
import { uploadEvent } from '../utils/storage.js'

const router = express.Router()

router.post("/upload", (req,res)=>{
    uploadEvent.single('image')(req, res, async function (err){
    if (err) {
      console.error('⛔ Multer greška:', err);
      return res.status(500).json({
        error: 'Greška u upload middleware-u',
        message: err.message,
        stack: err.stack
      });
    }
     try {
        const event = new Event({
            imageUrl: req.file.path,
            description: req.body.description || '',
            title: req.body.title,
            date: req.body.date
        });

        await event.save();

        res.json({ message: 'Uspešno!', event });
    } catch (saveError) {
      console.error(' Greška pri čuvanju u bazi:', saveError);
      res.status(500).json({
        error: saveError.message,
        stack: saveError.stack
      });
    }
})
})

router.get('/getAll', async(req, res) => {
    try{
        const events = await Event.find()
        res.json(events)
    }
    catch(error){
        console.log(error.message)
        res.status(500).json({error:error.message})
    }
})

export default router