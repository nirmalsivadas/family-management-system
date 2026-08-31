import {useCallback,useState,useEffect} from 'react';
import {Link, useSearchParams} from 'react-router-dom';
import './ViewFamilies.css';
import api from '../api/axios';
import { formatStatus } from '../utils/registerFamily';
import { normalizePageResponse } from '../utils/pageResponse';

const STATUSES = ['ALL', 'PENDING', 'CONFIRMED', 'REJECTED'];

function ViewFamilies(){
  const [searchParams, setSearchParams] = useSearchParams();
  const [families, setFamilies] = useState([]);
  const [loading,setLoading] = useState(true);
  const [total,setTotal] = useState(0);
  const [totalPages,setTotalPages] = useState(0);
  const [error,setError] = useState('');
  const status = searchParams.get('status') || 'ALL';
  const query = searchParams.get('query') || '';
  const page = Number(searchParams.get('page') || 0);
  const [searchInput,setSearchInput] = useState(query);

  const updateParams = useCallback((next)=>{
    const params = {
      status,
      query,
      page: String(page),
      ...next,
    };
    Object.keys(params).forEach((key)=>{
      if(key !== 'status' && !params[key]){
        delete params[key];
      }
      if(key === 'page' && params[key] === '0'){
        delete params[key];
      }
    });
    if(params.status === 'ALL'){
      delete params.status;
    }
    setSearchParams(params);
  },[page, query, setSearchParams, status]);

  useEffect(()=>{
    setSearchInput(query);
  },[query]);

  useEffect(()=>{
    const nextQuery = searchInput.trim();
    if(nextQuery === query){
      return undefined;
    }

    const timeoutId = window.setTimeout(()=>{
      updateParams({query: nextQuery, page: '0'});
    }, 350);

    return () => window.clearTimeout(timeoutId);
  },[searchInput, query, updateParams]);

  useEffect(() => {
    const controller = new AbortController();
    let active = true;
    const user = JSON.parse(localStorage.getItem('user'));
    const userId = user?.id;
    if(!userId){
      setFamilies([]);
      setTotal(0);
      setTotalPages(0);
      setLoading(false)
      return;
    }
    setLoading(true);
    setError('');
    api.get('/family/view-families', {
      params: { userId, status, page, size: 8, query: query || undefined },
      signal: controller.signal
    })
    .then((response)=>{
      if(!active){
        return;
      }
      const pageData = normalizePageResponse(response.data.data);
      setFamilies(pageData.content);
      setTotal(pageData.totalElements);
      setTotalPages(pageData.totalPages);
      setError('');
    }).catch((err)=>{
      if(!active){
        return;
      }
      if(err.code === 'ERR_CANCELED'){
        return;
      }
      console.error("Error fetching families:", err);
      setError('Unable to load families.');
      setFamilies([]);
      setTotal(0);
      setTotalPages(0);
    }).finally(()=>{
      if(active){
        setLoading(false);
      }
    });

    return () => {
      active = false;
      controller.abort();
    };
  },[status, query, page])

  return(
    <div className='view-families-container'>
      <div className="page-heading">
        <div>
          <h1>Families</h1>
          <p>{total} total registrations</p>
        </div>
        <Link className="primary-action" to="/register-family">+ Register New Family</Link>
      </div>
      <div className="list-toolbar">
        <div className="status-filters">
          {STATUSES.map((item)=>(
            <button
              key={item}
              type="button"
              className={status === item ? 'active' : ''}
              onClick={()=>updateParams({status: item, page: '0'})}
            >
              {item === 'ALL' ? 'All' : formatStatus(item)}
            </button>
          ))}
        </div>
        <form
          className="list-search"
          onSubmit={(event)=>{
            event.preventDefault();
            updateParams({query: searchInput.trim(), page: '0'});
          }}
        >
          <input
            value={searchInput}
            onChange={(event)=>setSearchInput(event.target.value)}
            placeholder="Search family, member, mobile, or membership #"
          />
          {searchInput && (
            <button
              type="button"
              className="search-clear"
              aria-label="Clear family search"
              onClick={()=>setSearchInput('')}
            >
              Clear
            </button>
          )}
          <button type="submit">Search</button>
        </form>
      </div>
      {error && <p className="empty-state">{error}</p>}
      <div className="data-panel families-list">
        <div className="families-row families-header">
          <span>Membership #</span>
          <span>Family Head</span>
          <span>Family Name</span>
          <span>Members</span>
          <span>Status</span>
          <span>Actions</span>
        </div>
        {loading && <p className="empty-state">Loading families...</p>}
        {!loading && families.map((family, index)=>(
          <div key={family.membershipId || `${page}-${index}`} className='families-row'>
            <strong>{family.membershipId}</strong>
            <span>{family.familyHead}</span>
            <span>{family.familyName}</span>
            <span>{family.numberOfFamilyMembers}</span>
            <span className={`status-pill ${family.status?.toLowerCase()}`}>{formatStatus(family.status)}</span>
            <span className="row-actions">
              <Link to={`/view-family/${family.membershipId}`}>View</Link>
              <Link to={`/update-family/${family.membershipId}`}>Edit</Link>
            </span>
          </div>
        ))}
        {!loading && families.length === 0 && <p className="empty-state">No families found.</p>}
      </div>
      {!loading && totalPages > 1 && (
        <div className="pager">
          <button type="button" disabled={page <= 0} onClick={()=>updateParams({page: String(page - 1)})}>Previous</button>
          <span>Page {page + 1} of {totalPages}</span>
          <button type="button" disabled={page + 1 >= totalPages} onClick={()=>updateParams({page: String(page + 1)})}>Next</button>
        </div>
      )}
    </div>
  );
}

export default ViewFamilies;
