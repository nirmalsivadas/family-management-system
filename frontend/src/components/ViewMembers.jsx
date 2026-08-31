import React,{useState,useEffect} from 'react';
import {Link, useSearchParams} from 'react-router-dom';
import './ViewMembers.css';
import api from '../api/axios';
import { formatStatus } from '../utils/registerFamily';

function formatRelationship(value) {
  if (!value) {
    return '-';
  }
  return String(value)
    .toLowerCase()
    .replaceAll('_', ' ')
    .replace(/\b\w/g, (letter) => letter.toUpperCase());
}

function ViewMembers(){
  const [searchParams,setSearchParams] = useSearchParams();
  const[familyMembers,setFamilyMembers] = useState([]);
  const[loading,setLoading] = useState(true);
  const [total,setTotal] = useState(0);
  const [totalPages,setTotalPages] = useState(0);
  const query = searchParams.get('query') || '';
  const page = Number(searchParams.get('page') || 0);
  const [searchInput,setSearchInput] = useState(query);

  useEffect(()=>{
    setSearchInput(query);
  },[query]);

  useEffect(()=>{
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;

    if(!userId){
      setLoading(false);
      return;
    }
    setLoading(true);
    api.get('/family/view-members', {
      params: { userId, page, size: 8, query: query || undefined }
    }).then((response)=>{
      const pageData = response.data.data;
      setFamilyMembers(pageData.content ?? pageData);
      setTotal(pageData.totalElements ?? (pageData.content?.length || 0));
      setTotalPages(pageData.totalPages ?? 1);
      setLoading(false);
    }).catch((err)=>{
      console.error("Error fetching families:", err);
        setLoading(false);
    })
  },[query, page])

  function updateParams(next){
    const params = { query, page: String(page), ...next };
    if(!params.query){
      delete params.query;
    }
    if(params.page === '0'){
      delete params.page;
    }
    setSearchParams(params);
  }

  if(loading){
    return <div className='view-members-container'>Loading members....</div>
  }
  return(
    <div className='view-members-container'>
      <div className="page-heading">
        <div>
          <h1>Members</h1>
          <p>{total} members</p>
        </div>
      </div>
      <form
        className="list-search members-search"
        onSubmit={(event)=>{
          event.preventDefault();
          updateParams({query: searchInput.trim(), page: '0'});
        }}
      >
        <input
          value={searchInput}
          onChange={(event)=>setSearchInput(event.target.value)}
          placeholder="Search members, families, or membership #"
        />
        <button type="submit">Search</button>
      </form>
      <div className='data-panel members-list'>
        <div className="members-row members-header">
          <span>Name</span>
          <span>Relationship</span>
          <span>Family</span>
          <span>Membership #</span>
          <span>Occupation</span>
          <span>Mobile</span>
          <span>Status</span>
          <span>Action</span>
        </div>
        {familyMembers.map((member) => (
          <div key={`${member.memberShipId}-${member.name}`} className='members-row'>
            <strong>{member.name}</strong>
            <span>{formatRelationship(member.relationShip)}</span>
            <span>{member.familyName}</span>
            <span>{member.memberShipId}</span>
            <span>{member.occupation}</span>
            <span>{member.mobileNumber}</span>
            <span className={`status-pill ${member.status?.toLowerCase()}`}>{formatStatus(member.status)}</span>
            <Link to={`/view-family/${member.memberShipId}`}>View</Link>
          </div>
        ))}
        {familyMembers.length === 0 && <p className="empty-state">No members found.</p>}
      </div>
      {totalPages > 1 && (
        <div className="pager">
          <button type="button" disabled={page <= 0} onClick={()=>updateParams({page: String(page - 1)})}>Previous</button>
          <span>Page {page + 1} of {totalPages}</span>
          <button type="button" disabled={page + 1 >= totalPages} onClick={()=>updateParams({page: String(page + 1)})}>Next</button>
        </div>
      )}
    </div>
  )
}

export default ViewMembers;
