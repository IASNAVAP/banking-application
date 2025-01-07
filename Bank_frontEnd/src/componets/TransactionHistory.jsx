import React,{useEffect,useState} from "react";

const TranscationHistory=({accountNumber,token})=>{
    const [transaction,setTransaction]=useState([]);
    const [error,setError]=useState("");

    useEffect(()=>{
        const fetchTranscation=async()=>{
            try{
                const response=await fetch(`http://localhost:8081/bankstatement/getstatement?accountnumber=${accountNumber}`,
                    {
                        method:"GET",
                        headers:{
                            Authorzation:`Bearer ${token}`,
                        },
                    }
                );
                if(!response.ok){
                    throw new Error("Failed to fetch transaction history");
                }
                const data = await response.json();
                setTransaction(data.slice(0,5));
                
            }catch(error){
                setError(error.message);
            }
        };
        fetchTranscation();
    },[accountNumber,token]);

    return(
        <div className="mt-4">
            <h3>Recent Transactions</h3>
            {error ?(<div className="alert alert-danger">{error}</div>)
            :transaction.length>0?(
                <table className="table table-striped mt-3">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Transaction Type</th>
                            <th>Amount (₹)</th>
                            <th>Status</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transaction.map((transaction,index)=>(
                            <tr key={transaction.transactionId}>
                                <td>{index + 1}</td>
                                <td>{transaction.transactionType}</td>
                                <td>{transaction.amount.toFixed(2)}</td>
                                <td>{transaction.status}</td>
                                <td>{transaction.createdAt}</td>
                             </tr>   
                        ))}
                    </tbody>
                </table>    
            ):(<div>No transactions available.</div>)
        }
        </div>
    )
}

export default TranscationHistory;