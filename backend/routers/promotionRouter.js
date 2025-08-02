import express from 'express';
import Promotion from '../models/promotion.js';
import { uploadPromo } from '../utils/storage.js';
const router = express.Router();

router.post('/upload', (req, res) => {
  uploadPromo.single('image')(req, res, async function (err) {
    if (err) {
      console.error('⛔ Multer greška:', err);
      return res.status(500).json({
        error: 'Greška u upload middleware-u',
        message: err.message,
        stack: err.stack
      });
    }

    console.log('✅ Usao u try blok');
    console.log('📁 Fajl:', req.file);
    console.log('📝 Body:', req.body);

    try {
      const promotion = new Promotion({
        imageUrl: req.file.path,
        description: req.body.description || '',
      });

      await promotion.save();

      res.json({ message: 'Uspešno!', promotion });
    } catch (saveError) {
      console.error(' Greška pri čuvanju u bazi:', saveError);
      res.status(500).json({
        error: saveError.message,
        stack: saveError.stack
      });
    }
  });
});
router.get('/getAll', async(req, res)=>{
  console.log("Usli smo u get all promotions")
    try{
        const promotions = await Promotion.find()
        console.log("uspeh")
        res.json(promotions)
    }
    catch(error){
      console.log("Neuspeh"+ error.message)
      res.status(500).json({error:error.message})
    }
})
export default router
