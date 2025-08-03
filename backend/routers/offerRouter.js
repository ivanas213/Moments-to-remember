import express from 'express'
import Offer from '../models/offer.js'
import { uploadOffer } from '../utils/storage.js'

const router = express.Router()

router.post("/upload", (req,res)=>{
    uploadOffer.single('image')(req, res, async function (err){
    if (err) {
      console.error('⛔ Multer greška:', err);
      return res.status(500).json({
        error: 'Greška u upload middleware-u',
        message: err.message,
        stack: err.stack
      });
    }
     try {
        const offer = new Offer({
            offerImageUrl: req.file.path,
            minGuests: req.body.minGuests,
            maxGuests: req.body.maxGuests,
            price: req.body.price,
            hall: req.body.hall,
            name: req.body.name
        });

        await offer.save();

        res.json({ message: 'Uspešno!', offer });
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
        const offers = await Offer.find()
        res.json(offers)
    }
    catch(error){
        console.log(error.message)
        res.status(500).json({error:error.message})
    }
})

export default router

