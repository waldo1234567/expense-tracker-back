/**

Soal Latihan: Menentukan Kategori Usia

Buatlah fungsi yang dapat menentukan kategori usia seseorang berdasarkan rentang usia sebagai berikut:
1. 0-12 tahun: "Anak-anak"
2. 13-19 tahun: "Remaja"
3. 20-35 tahun: "Dewasa"
4. 36 tahun ke atas: "Lanjut Usia"

Kalian diminta membuat fungsi kategoriUsia(umur) yang menerima parameter:
1. umur (number): Usia seseorang dalam tahun.

Fungsi ini harus mengembalikan kategori usia berdasarkan rentang usia yang diberikan.

*/

function kategoriUsia(umur){
    if(umur <= 12){
        console.log("Anak-anak");
    }else if(umur <= 19){
        console.log("Remaja");
    }else if(umur <= 35){
        console.log("Dewasa");
    }else{
        console.log("Lanjut Usia");
    }
}

kategoriUsia(47);