import express from 'express'
import Hall from '../models/hall.js'
import { uploadHall } from '../utils/storage.js'
const router = express.Router()

router.post("/upload", (req,res)=>{
    uploadHall.single('image')(req, res, async function (err){
    if (err) {
      console.error('⛔ Multer greška:', err);
      return res.status(500).json({
        error: 'Greška u upload middleware-u',
        message: err.message,
        stack: err.stack
      });
    }
     try {
        const hall = new Hall({
            imageUrl: req.file.path,
            name: req.body.name,
            capacity: req.body.capacity,
            address: req.body.address,
            description: req.body.description
        });

        await hall.save();

        res.json({ message: 'Uspešno!', hall });
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
        const halls = await Hall.find()
        res.json(halls)
    }
    catch(error){
        console.log(error.message)
        res.status(500).json({error:error.message})
    }
})

export default router